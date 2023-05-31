package net.chimhaha.clone.annotation.validator;

import net.chimhaha.clone.annotation.MultipartExist;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MultipartExistValidator implements ConstraintValidator<MultipartExist, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> value, ConstraintValidatorContext context) {
        for(MultipartFile file : value) {
            if(file.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
