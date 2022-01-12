package com.icia.member.memberproject;

import com.icia.member.memberproject.dto.MemberDetailDTO;
import com.icia.member.memberproject.dto.MemberLoginDTO;
import com.icia.member.memberproject.dto.MemberMapperDTO;
import com.icia.member.memberproject.dto.MemberSaveDTO;
import com.icia.member.memberproject.repository.MemberMapperRepository;
import com.icia.member.memberproject.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberTest {
    /*
        MemberServiceImpl.save() 메서드가 잘 동작하는지를 테스트

        회원가입테스트
        save.html 에서 회원정보 입력 후 가입 클릭
        DB 확인
     */
    @Autowired
    private MemberService ms;

    @Autowired
    private MemberMapperRepository mmr;

// unit Test

    @Test
    @DisplayName("회원가입테스트")
    public void memberSaveTest() {
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO();
        memberSaveDTO.setMemberEmail("테스트회원이메일1");
        memberSaveDTO.setMemberPassword("테스트비번1");
        memberSaveDTO.setMemberName("테스트회원이름1");

        ms.save(memberSaveDTO);

    }

    @Test
    @Transactional //테스트 시작할 때 새로운 트랜잭션 시작
    @Rollback // 테스트 종료 후 롤백 수행
    @DisplayName("회원조회테스트")
    public void memberDetailTest() {
        // given: 테스트 조건 설정
            // 1. 새로운 회원을 등록하고 해당회원의 번호(member_id)를 가져옴
        // 1.1 테스트용 데이터 객체 생성
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("조회용회원이메일", "조회용회원비번", "조회용회원이름");
        //1.2 테스트용 데이터를 DB에 저장하고 member_id를 가져옴
        Long memberId = ms.save(memberSaveDTO);

        // when: 테스트 수행
            // 2. 위에서 가져온 회원번호를 가지고 조회 기능 수행
        MemberDetailDTO findMember = ms.findById(memberId);

        // then: 테스트 결과 검증
            // 3. 1번에서 가입한 회원의 정보와 2번에서 조회한 회원의 정보가 일치하면 테스트 통과 일치하지 않으면 테스트 실패
        // memberSaveDTO의 이메일 값과 findMember의 이메일 값이 일치하는지 확인
        assertThat(memberSaveDTO.getMemberEmail()).isEqualTo(findMember.getMemberEmail());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("로그인테스트")
    public void loginTest() {
        /*
            1. 테스트용 회원가입(MemberSaveDTO)
            2. 로그인용 객체 생성(MeemberLoginDTO)
            1.2. 수행할 때 동일한 이메일, 패스워드를 사용하도록 함.
            3. 로그인 수행
            4. 로그인 결과가 true인지 확인
         */
        //given
        final String testMemberEmail = "로그인테스트이메일";
        final String testMemberPassword = "로그인테스트비밀번호";
        final String testMemberName = "로그인테스트이름";
        // 1.
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO(testMemberEmail, testMemberPassword, testMemberName);
        ms.save(memberSaveDTO);
        //when
        // 2.
        MemberLoginDTO memberLoginDTO = new MemberLoginDTO(testMemberEmail, testMemberPassword);
        boolean loginResult = ms.login(memberLoginDTO);
        //then
        assertThat(loginResult).isEqualTo(true);

    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("회원목록 테스트")
    public void memberListTest() {
        /*
            member_table에 아무 데이터가 없는 상태에서
            3명의 회원을 가입시킨 후 memberList 사이즈를 조회하여 3이면 테스트 통과
         */

        // 1. 3명의 회원을 가입
//        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("리스트회원1", "리스트회원pw1", "리스트회원이름1");
//        ms.save(memberSaveDTO);
//        memberSaveDTO = new MemberSaveDTO("리스트회원2", "리스트회원pw2", "리스트회원이름2");
//        ms.save(memberSaveDTO);
//        memberSaveDTO = new MemberSaveDTO("리스트회원3", "리스트회원pw3", "리스트회원이름3");
//        ms.save(memberSaveDTO);

//        for(int i=1; i<=3; i++) {
////            ms.save(new MemberSaveDTO("리스트회원"+i, "리스트회원pw"+i, "리스트회원이름"+i));
//            // 위 한줄과 아래 두줄 동일
//            MemberSaveDTO memberSaveDTO = new MemberSaveDTO("리스트회원"+i, "리스트회원pw"+i, "리스트회원이름"+i);
//            ms.save(memberSaveDTO);
//        }

        // IntStream 방식, Arrow Function(화살표함수)
        IntStream.rangeClosed(1, 3).forEach(i -> {
            ms.save(new MemberSaveDTO("리스트회원"+i, "리스트회원pw"+i, "리스트회원이름"+i));
        });

        List<MemberDetailDTO> list = ms.findAll();
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("조회 테스트")
    public void memberListTest2() {

        for (int i=1; i<=3; i++) {
            ms.save(new MemberSaveDTO("조회용이메일"+i, "조회용비밀번호"+i,"조회용이름"+i));
        }

        List<MemberDetailDTO> memberDetailDTOList = ms.findAll();

        assertThat(ms.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("회원데이터생성")
    public void newMembers() {
        IntStream.rangeClosed(1, 15).forEach(i -> {
            ms.save(new MemberSaveDTO("email" +i, "pw"+i, "name"+i));
        });
    }

    /*
        회원삭제 테스트 코드를 만들어봅시다.
        회원삭제 시나리오를 작성해보고 코드도 짜보기
     */
    @Test
    @Transactional
    @Rollback
    @DisplayName("회원삭제 테스트")
    public void memberDeleteTest() {
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("삭제용회원이메일1", "삭제용회원비번1", "삭제용회원이름1");
        Long memberId = ms.save(memberSaveDTO);
        System.out.println(ms.findById(memberId));

        ms.deleteById(memberId);
        //System.out.println(ms.findById(memberId));
        // 삭제한 회원의 id로 조회를 시도했을 때 null 이어야 테스트 통과
        // NouSchElementException은 무시하고 테스트를 수행
        assertThrows(NoSuchElementException.class, () -> {
           assertThat(ms.findById(memberId)).isNull(); // 삭제회원 id 조회결과가 Null 이면 테스트 통과
        });
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("회원수정 테스트")
    public void memberUpdateTest() {
        /*
            1. 신규회원 등록
            2. 신규등록한 회원에 대한 이름 수정
            3. 신규등록시 사용한 이름과 DB에 저장된 이름이 일치하는지 판단
            4. 일치하지 않아야 테스트 통과
         */
        // 1.
        String memberEmail = "수정회원1";
        String memberPassword = "수정비번1";
        String memberName = "수정이름1";
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO(memberEmail, memberPassword, memberName);
        Long saveMemberId = ms.save(memberSaveDTO);

        // 가입 후 DB에서 이름 조회
        String saveMemberName = ms.findById(saveMemberId).getMemberName();

        // 2.
        String updateName = "수정용이름1";
        MemberDetailDTO updateMember = new MemberDetailDTO(saveMemberId, memberEmail, memberPassword, updateName);
        ms.update(updateMember);
        // 수정 후 DB에서 이름 조회
        String updateMemberName = ms.findById(saveMemberId).getMemberName();

        // 3. 4.
        assertThat(saveMemberName).isNotEqualTo(updateMemberName);
    }

    @Test
    @Transactional
    @DisplayName("mybatis 목록 출력 테스트")
    public void memberListTest3() {
        List<MemberMapperDTO> memberList = mmr.memberList();
        for(MemberMapperDTO m: memberList) {
            System.out.println("m.toString() = "+m);
        }
        List<MemberMapperDTO> memberList2 = mmr.memberList2();
        for(MemberMapperDTO m: memberList2) {
            System.out.println("m.toString() = "+m);
        }
    }

    @Test
    @DisplayName("mybatis 회원가입 테스트")
    public void memberSaveTest2() {
        MemberMapperDTO memberMapperDTO = new MemberMapperDTO("회원이메일111", "회원비번111", "회원이름111");
        mmr.save(memberMapperDTO);
        MemberMapperDTO memberMapperDTO2 = new MemberMapperDTO("회원이메일22", "회원비번22", "회원이름22");
        mmr.save2(memberMapperDTO2);
    }
}
