package au.com.new1step.herbalist.util;

import au.com.new1step.herbalist.exception.EntityHasIdException;
import au.com.new1step.herbalist.exception.EntityNotFoundException;
import au.com.new1step.herbalist.exception.EntityNotHaveIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
@Slf4j
public class ThrowExceptionUtil {
    private MessageSource messageSource;
    public ThrowExceptionUtil(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    public String throwEntityNotFoundException(String entityMsgCode, String entityParameter) throws EntityNotFoundException {
        Locale current = LocaleContextHolder.getLocale();
        log.debug("Current locale is {}", current);
        String entity = messageSource.getMessage(entityMsgCode, null, current);
        String msg = messageSource.getMessage("entity.not.found.message", new Object[] { entity, entityParameter }, current);
        throw new EntityNotFoundException(msg);
    }

    public String throwEntityNotHaveIdException(String entityMsgCode, String entityParameter) throws EntityNotHaveIdException {
        Locale current = LocaleContextHolder.getLocale();
        log.debug("Current locale is {}", current);
        String entity = messageSource.getMessage(entityMsgCode, null, current);
        String msg = messageSource.getMessage("entity.not.have.id.message", new Object[] { entity, entityParameter }, current);
        throw new EntityNotHaveIdException(msg);
    }

    public String throwEntityHasIdException(String entityMsgCode, String entityParameter) throws EntityHasIdException {
        Locale current = LocaleContextHolder.getLocale();
        log.debug("Current locale is {}", current);
        String entity = messageSource.getMessage(entityMsgCode, null, current);
        String msg = messageSource.getMessage("entity.have.id.message", new Object[] { entity, entityParameter }, current);
        throw new EntityHasIdException(msg);
    }
}
