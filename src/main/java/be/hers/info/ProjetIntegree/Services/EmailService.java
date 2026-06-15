package be.hers.info.ProjetIntegree.Services;

import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending email notifications to newly created users.
 *
 * @author Nicolas Jean-François
 * @reviewer Wellinger Chloé, Halet Louis
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a welcome email to a newly created user containing their login credentials.
     *
     * @param toEmail   the recipient email address
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param login     the generated login
     * @param password  the temporary password (plain text, before hashing)
     * @param role      the user's role (Interprète, Bénéficiaire, Résa, Coordinatrice)
     */
    public void sendWelcomeEmail(String toEmail, String firstName, String lastName, String login, String password, String role) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Bienvenue sur Lexia — Vos identifiants de connexion");

            String html = """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1.5px solid #e8e0f5; border-radius: 16px; overflow: hidden; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.07);">
            <div style="background-color: #6c5ce7; padding: 28px; text-align: center;">
                <h1 style="color: white; margin: 0; font-size: 24px; font-weight: 700;">Lexia</h1>
                <p style="color: #e8e0f5; margin: 4px 0 0 0; font-size: 14px;">HERS — Section informatique</p>
            </div>
            <div style="padding: 32px;">
                <p style="font-size: 16px; color: #333;">Bonjour <strong>%s %s</strong>,</p>
                <p style="color: #555;">Votre compte a été créé sur l'application <strong>Lexia</strong>. Voici vos informations de connexion :</p>
                <div style="background-color: #f3effe; border-left: 4px solid #6c5ce7; padding: 16px; border-radius: 8px; margin: 24px 0;">
                    <table style="width: 100%%; border-collapse: collapse;">
                        <tr>
                            <td style="padding: 6px 12px; color: #888; font-size: 13px; width: 130px;">Rôle</td>
                            <td style="padding: 6px 12px; font-weight: 700; color: #593196;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 6px 12px; color: #888; font-size: 13px;">Login</td>
                            <td style="padding: 6px 12px; font-weight: 700; color: #333; font-family: monospace; font-size: 15px;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 6px 12px; color: #888; font-size: 13px;">Mot de passe</td>
                            <td style="padding: 6px 12px; font-weight: 700; color: #333; font-family: monospace; font-size: 15px;">%s</td>
                        </tr>
                    </table>
                </div>
                <p style="color: #e67e22; font-size: 13px; background-color: #fff8f0; border-radius: 6px; padding: 10px 12px; margin: 0;">
                    ⚠️ Nous vous recommandons de changer votre mot de passe lors de votre première connexion.
                </p>
            </div>
            <div style="background-color: #f3effe; padding: 16px; text-align: center; font-size: 12px; color: #888;">
                Cordialement, <strong style="color: #593196;">L'équipe Lexia — HERS</strong>
            </div>
        </div>
        """.formatted(firstName, lastName, role, login, password);

            helper.setText(html, true);
            mailSender.send(message);
            logger.info("Email de bienvenue envoyé à {}", toEmail);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {}", toEmail, e);
        }
    }
}
