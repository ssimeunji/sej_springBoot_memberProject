package com.icia.member.memberproject.controller;

import com.icia.member.memberproject.dto.MemberDetailDTO;
import com.icia.member.memberproject.dto.MemberLoginDTO;
import com.icia.member.memberproject.dto.MemberSaveDTO;
import com.icia.member.memberproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.icia.member.memberproject.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/member/*")  // 모든 요청을 다 받아줌 (Http protocol : get, post, put, delete)
@RequiredArgsConstructor // final이 붙은 필드로만 생성자 를 만들어줌 (생성자 주입)
public class MemberController {

    private final MemberService ms;

    // 회원가입 폼
    @GetMapping("save")
    public String saveForm() {
        return "member/save";
    }

    // 회원가입
    @PostMapping("save")
    public String save(@ModelAttribute MemberSaveDTO memberSaveDTO) {
        Long memberId = ms.save(memberSaveDTO);
        return "member/login";
    }

    // 로그인 폼
    @GetMapping("login")
    public String loginForm() {
        return "member/login";
    }

    // 로그인
    @PostMapping("login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session) {
        System.out.println("MemberController.loginForm");
//        System.out.println("redirectURL = "+redirectURL);
        if (ms.login(memberLoginDTO)) {
//            session.setAttribute("loginEmail",memberLoginDTO.getMemberEmail());
            // 아래처럼 상수를 선언해 사용할 수 있음, 아무거나 편한걸로 사용
            session.setAttribute(LOGIN_EMAIL,memberLoginDTO.getMemberEmail());
            String redirectURL = (String) session.getAttribute("redirectURL");
            // 인터셉터를 거쳐서 오면 redirectURL에 값이 있을 것이고 그냥 로그인을 해서 오면 redirectURL에 값이 없을 것임
            // 따라서 if else로 구분을 해줌
            if(redirectURL != null) {
                return "redirect:"+redirectURL; // 사용자가 요청한 주소로 보내주기 위해
            } else {
                return "redirect:/";
            }
        } else {
            return "member/login";
        }
    }

    // 로그아웃
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // 회원목록
    @GetMapping("findAll")
    public String findAll(Model model) {
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList", memberList);
        return "member/findAll";
    }

    // 상세조회
    // /member/2, /member/15 => /member/{memberId}
    // @PathVariavle : 경로상에 있는 변수를 가져올 때 사용
    @GetMapping("{memberId}")
    public String findById(@PathVariable("memberId") Long memberId, Model model) {
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);
        return "member/findById";
    }

    @PostMapping("{memberId}") // @PathVariable() 안에 변수 이름과 선언하는 변수 이름이 같다면 생략가능.
    public @ResponseBody MemberDetailDTO detail(@PathVariable Long memberId) {
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        return memberDetailDTO;
    }

    // 회원삭제(/member/delete/5)
    @GetMapping("delete/{memberId}")
    public String deleteById(@PathVariable("memberId") Long memberId) {
        ms.deleteById(memberId);
        return "redirect:/member/";
    }

    // 회원삭제 (/member/5)
    @DeleteMapping("{memberId}")
    public ResponseEntity deleteById2(@PathVariable Long memberId) {
        ms.deleteById(memberId);
        /*
            // 단순 화면 출력이 아닌 데이터를 리턴하고자 할 때 사용하는 리턴방식
            ResponseEntity: 데이터 & 상태코드(200, 400, 404, 405, 500 등)를 함께 리턴할 수 있음.
            @ResponseBody: 데이터를 리턴할 수 있음.
         */
        // 200 코드를 리턴
        return new ResponseEntity(HttpStatus.OK);
    }

    // 수정화면 요청
    @GetMapping("update")
    public String updateForm(Model model, HttpSession session) {
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
        MemberDetailDTO member = ms.findByEmail(memberEmail);
        model.addAttribute("member", member);
        return "member/update";
    }
    // 수정 처리(post)
    @PostMapping("update")
    public String update(@ModelAttribute MemberDetailDTO memberDetailDTO) {
        Long memberId = ms.update(memberDetailDTO);
        // 수정 완료 후 해당회원의 상세페이지(findById.html) 출력
        return "redirect:/member/"+memberDetailDTO.getMemberId();
    }

    // 수정처리(put)
    @PutMapping("{memberId}")
    // json 으로 데이터가 전달되면 @RequestBody로 받아줘야함
    public ResponseEntity update2(@RequestBody MemberDetailDTO memberDetailDTO) {
        Long memberId = ms.update(memberDetailDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
