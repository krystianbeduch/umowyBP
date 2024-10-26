package pl.bpwesley.TourOperator.email.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;
import pl.bpwesley.TourOperator.email.repository.EmailTemplateVariableRepository;
import pl.bpwesley.TourOperator.email.repository.VariableRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VariableService {
    private final EmailTemplateVariableRepository emailTemplateVariableRepository;
    private final VariableRepository variableRepository;

    @Autowired
    public VariableService(EmailTemplateVariableRepository emailTemplateVariableRepository, VariableRepository variableRepository) {
        this.emailTemplateVariableRepository = emailTemplateVariableRepository;
        this.variableRepository = variableRepository;
    }

    public List<String> getVariableList() {
        return variableRepository.findAllNames();
    }

    public String replaceVariables(String content, List<String> variableNames) {
        Map<String, String> variableValues = new HashMap<>();

        for (String variableName : variableNames) {
            String value = variableRepository.findValueByName(variableName);
            if (value != null) {
                variableValues.put(variableName, value);
            }
        }

        for (Map.Entry<String, String> entry : variableValues.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return content;
    }

    public List<EmailTemplateVariable> getEmailTemplateVariablesByTemplateId(Long emailTemplateId) {
        // aktualnie nie potrzebna
        return emailTemplateVariableRepository.findByEmailTemplate_EmailTemplateId(emailTemplateId);
    }
}
