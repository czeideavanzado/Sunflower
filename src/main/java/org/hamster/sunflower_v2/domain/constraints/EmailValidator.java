package org.hamster.sunflower_v2.domain.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ONB-CZEIDE on 02/26/2018
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

//    private Pattern pattern;
//    private Matcher matcher;
//    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+ (.[_A-Za-z0-9-]+)*@"
//            + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    private int min;

    @Override
    public void initialize(ValidEmail validEmail) {
        min = validEmail.min();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
//        return (validateEmail(email));
        if (email.length() < min) {
            return false;
        }

        if (!org.apache.commons.validator.routines.EmailValidator.getInstance(false).isValid(email)) {
            return false;
        }

        return true;
    }

//    private boolean validateEmail(String email) {
//        pattern = Pattern.compile(EMAIL_PATTERN);
//        matcher = pattern.matcher(email);
//        return matcher.matches();
//    }
}
