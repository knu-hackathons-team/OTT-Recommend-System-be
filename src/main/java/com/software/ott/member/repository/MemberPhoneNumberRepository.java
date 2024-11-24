package com.software.ott.member.repository;

import com.software.ott.member.entity.MemberPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPhoneNumberRepository extends JpaRepository<MemberPhoneNumber, Long> {

    boolean existsByMemberId(Long memberId);

}
