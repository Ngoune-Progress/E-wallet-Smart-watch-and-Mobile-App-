package com.example.PayMe.service;

import com.example.PayMe.Email.EmailSender;
import com.example.PayMe.Entity.UserEntity;
import com.example.PayMe.models.AuthenticationRequest;
import com.example.PayMe.models.RegistrationRequest;
import com.example.PayMe.repository.UserRepository;
import com.example.PayMe.security.EmailValidator;
import com.example.PayMe.security.token.ConfirmationToken;
import com.example.PayMe.security.token.ConfirmationTokenService;
import com.example.PayMe.security.token.OTP;
import com.example.PayMe.security.token.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPVERIFICATION {
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final OTPService confirmTokenService;
    private final UserRepository userRepository;
    private final EmailSender emailSender;

    public String login(AuthenticationRequest request){
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (isValidEmail) {
            Random ran = new Random();
            int x = ran.nextInt(6) + 5;

//
//            String tokenForNewUser = appUserService.signUpUser(new UserEntity(request.getFirstName(),
//                    request.getLastName(),
//                    request.getEmail(),
//                    request.getPassword(),
//                    request.getTel()
//                    //,
////                    AppUserRole.USER)
//            ));

            //Since, we are running the spring boot application in localhost, we are hardcoding the
            //url of the server. We are creating a POST request with token param
//            String link = "http://localhost:8080/api/v1/registration/confirm?token=" + tokenForNewUser;
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime expiredAt  = createdAt.plusMinutes(15);
Optional<UserEntity> userEntity = userRepository.findByEmail(request.getEmail());
if(userEntity.isPresent()){
    UserEntity user = userEntity.get();
    confirmTokenService.saveConfirmationToken(OTP.builder().createdAt(createdAt)
            .expiredAt(expiredAt)
            .token(String.valueOf(x))
            .status(false)
            .build());
    emailSender.sendEmail(request.getEmail(),buildEmail(user.getFirstName(), String.valueOf(x)));

}
else {
    throw new IllegalStateException((String.format("Email %s, not valid", request.getEmail())));
}



            return String.valueOf(x);
        } else {
            throw new IllegalStateException(String.format("Email %s, not valid", request.getEmail()));
        }
    }

//    @Transactional
//    public String confirmToken(String token) {
//        Optional<ConfirmationToken> confirmToken = confirmTokenService.getToken(token);
//
//        if (confirmToken.isEmpty()) {
//            throw new IllegalStateException("Token not found!");
//        }
//
//        if (confirmToken.get().getConfirmedAt()!= null) {
//            throw new IllegalStateException("Email is already confirmed");
//        }
//
//        LocalDateTime expiresAt = confirmToken.get().getExpiredAt();
//
//        if (expiresAt.isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("Token is already expired!");
//        }
//
//        confirmTokenService.setConfirmedAt(token);
//        appUserService.enableAppUser(confirmToken.get().getUser().getEmail());
//
//        //Returning confirmation message if the token matches
//        return "Your email is confirmed. Thank you for using our service!";
//    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}