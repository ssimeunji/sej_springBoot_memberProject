package com.icia.member.memberproject.repository;

import com.icia.member.memberproject.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<해당Entity클래스이름, PK타입>
// JpaRepository 상속받아 쓸 때는 @Repository 안씀.
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 이메일을 조건으로 회원 조회 (select * from member_table where member_email=?)
    /*
        메서드 리턴타입: MemberEntity
        메서드 이름: findByMemberEmail
        메서드 매개변수: String memberEmail
     */
    MemberEntity findByMemberEmail(String memberEmail);
}
