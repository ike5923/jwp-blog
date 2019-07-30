package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.utils.model.ModelUtil;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;

@Controller
public class UserController {
    private final UserService userService;
    private final LoginService loginService;
    private final HttpSession session;

    public UserController(UserService userService, LoginService loginService, HttpSession session) {
        this.userService = userService;
        this.loginService = loginService;
        this.session = session;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        SessionUtil.removeAttribute(session, USER);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid UserRequestDto userRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }
        UserResponseDto userResponseDto = loginService.loginByEmailAndPwd(userRequestDto);
        SessionUtil.setAttribute(session, USER, userResponseDto);

        return "redirect:/";
    }

    @GetMapping("/users")
    public String users(Model model) {
        ModelUtil.addAttribute(model, "users", userService.findAll());

        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String editUser() {
        return "mypage-edit";
    }

    @PostMapping("/signup")
    public String addUser(@Valid UserRequestDto userRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }
        UserResponseDto userResponseDto = userService.addUser(userRequestDto);
        SessionUtil.setAttribute(session, USER, userResponseDto);

        return "redirect:/";
    }

    @PutMapping("/users")
    public String updateUser(@Valid UserRequestDto userRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "mypage";
        }
        UserResponseDto origin = (UserResponseDto) SessionUtil.getAttribute(session, USER);
        UserResponseDto userResponseDto = userService.updateUser(userRequestDto, origin);
        SessionUtil.setAttribute(session, USER, userResponseDto);

        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String deleteUser() {
        UserResponseDto userResponseDto = (UserResponseDto) SessionUtil.getAttribute(session, USER);
        userService.deleteUser(userResponseDto);
        SessionUtil.removeAttribute(session, USER);

        return "redirect:/";
    }
}
