package com.icia.member.memberproject.entity;

import com.icia.member.memberproject.dto.MemberDetailDTO;
import com.icia.member.memberproject.dto.MemberSaveDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_imcreament
    @Column(name = "member_id") // 별도 컬럼이름 지정할 때
    private Long id;

    // memberEmail: 크기 50, unique
    @Column(length = 50, unique = true) //카멜케이스는 자동으로 언더바 붙여버림
    private String memberEmail;

    // memberPassword: 크기 20
    @Column(length = 20)
    private String memberPassword;

    // Column 생략하면 default 크기 255로 지정됨
    private String memberName;

    /*
        DTO 클래스 객체를 전달받아 Entity 클래스 필드값으로 세팅하고
        Entity 객체를 리턴하는 메서드 선언

        static 메서드(정적메서드) : 클래스 메서드, 객체를 만들지 않고도 바로 호출 가능
    */
    public static MemberEntity saveMember(MemberSaveDTO memberSaveDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberSaveDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberSaveDTO.getMemberPassword());
        memberEntity.setMemberName(memberSaveDTO.getMemberName());
        return memberEntity;
    }

    // MemberDetailDTo -> MemberEntity 객체로 변환하기 위한 메서드
    public static MemberEntity toUpdateMember(MemberDetailDTO memberDetailDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDetailDTO.getMemberId());
        memberEntity.setMemberEmail(memberDetailDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDetailDTO.getMemberPassword());
        memberEntity.setMemberName(memberDetailDTO.getMemberName());
        return memberEntity;
    }
}
