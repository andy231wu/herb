package au.com.new1step.herbalist.util;

import au.com.new1step.herbalist.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Locale;

@Slf4j
public class MessageUtil {

   private MessageSource messageSource;

    public MessageUtil(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    public void addFeedbackMessage(RedirectAttributes attributes, String feedbackKey, String messageCode, Object... messageParameters) {
        log.debug("Adding feedback message with code: {} and params: {}", messageCode, messageParameters);
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        log.debug("Localized message is: {}", localizedFeedbackMessage);
        attributes.addFlashAttribute(feedbackKey, localizedFeedbackMessage);
    }

    private String getMessage(String messageCode, Object... messageParameters) {
        Locale current = LocaleContextHolder.getLocale();
        log.debug("Current locale is {}", current);
        return messageSource.getMessage(messageCode, messageParameters, current);
    }
}
