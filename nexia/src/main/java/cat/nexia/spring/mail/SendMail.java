package cat.nexia.spring.mail;

import cat.nexia.spring.utils.NexiaEnum;
import cat.nexia.spring.utils.NexiaUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.StringWriter;

@Service
public class SendMail {

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    private static final Log LOG = LogFactory.getLog(SendMail.class);

    public void send(String to,String cc,String cco,String asunto,String textoCorreo) {
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(asunto);
        message.setText(textoCorreo);
        mailSender.send(message);
        LOG.info(NexiaEnum.SEND_MAIL.getPhrase() + to);

    }

    public void sendWithAttach(String to,String cc,String cco,String asunto,String textoCorreo, String nombreFichero, byte[] bytes) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(asunto);
            helper.setText(textoCorreo, true);
            helper.addAttachment(nombreFichero, new ByteArrayResource(bytes));

            mailSender.send(message);
        } catch (Exception e) {
           StringWriter sw = NexiaUtils.getStringWriter(e);
           LOG.error(sw.toString());
        }

    }

    public  void sendEmailHtml (String to,String cc,String cco,String asunto,String textoCorreo) {
        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(asunto);
            helper.setText(textoCorreo, true);

            mailSender.send(message);
        } catch (Exception e) {
            StringWriter sw = NexiaUtils.getStringWriter(e);
            LOG.error(sw.toString());
        }

    }

}
