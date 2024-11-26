package com.software.ott.auth.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.software.ott.common.exception.BadRequestException;
import com.software.ott.common.exception.NotFoundException;
import com.software.ott.common.properties.CommonProperties;
import com.software.ott.member.entity.Member;
import com.software.ott.member.entity.MemberPhoneNumber;
import com.software.ott.member.repository.MemberPhoneNumberRepository;
import com.software.ott.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PhoneNumberAuthService {

    private final CommonProperties commonProperties;
    private final MemberPhoneNumberRepository memberPhoneNumberRepository;
    private final MemberRepository memberRepository;
    private final Map<String, String> hashStore = new ConcurrentHashMap<>();
    private final Map<String, Boolean> checkPhoneNumber = new ConcurrentHashMap<>();

    public PhoneNumberAuthService(CommonProperties commonProperties, MemberPhoneNumberRepository memberPhoneNumberRepository, MemberRepository memberRepository) {
        this.commonProperties = commonProperties;
        this.memberPhoneNumberRepository = memberPhoneNumberRepository;
        this.memberRepository = memberRepository;
    }

    public byte[] generateQRCodeAsByteArray(Long memberId, String phoneNumber) {
        if (memberPhoneNumberRepository.existsByMemberId(memberId)) {
            throw new BadRequestException("해당 멤버는 이미 인증된 전화번호가 있습니다.");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String hashKey = generateRandomHash();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(smsContent(hashKey), BarcodeFormat.QR_CODE, 250, 250);

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            hashStore.put(phoneNumber, hashKey);

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateRandomHash() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String smsContent(String hash) {
        return "sms:" + commonProperties.phoneNumber() + "?body=" + hash;
    }

    public void authPhoneNumber(Map<String, String> params) {
        String from = params.get("From");
        String body = params.get("Body");

        System.out.println(from);
        System.out.println(body);

        if (hashStore.containsKey(from) && hashStore.get(from).equals(body)) {
            hashStore.remove(from);
            if (!checkPhoneNumber.containsKey(from)) {
                checkPhoneNumber.put(from, true);
            }
        }
    }

    public void addPhoneNumber(Long memberId, String phoneNumber) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        if (checkPhoneNumber.containsKey(phoneNumber)) {
            memberPhoneNumberRepository.save(
                    MemberPhoneNumber.builder()
                            .phoneNumber(phoneNumber)
                            .member(member)
                            .build());

            checkPhoneNumber.remove(phoneNumber);
        } else {
            throw new BadRequestException("멤버 번호가 인증되지 않았거나, 이미 등록되었습니다.");
        }
    }
}
