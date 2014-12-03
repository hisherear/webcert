package se.inera.webcert.notifications.service;

import se.inera.webcert.persistence.intyg.model.Intyg;
import se.inera.webcert.persistence.intyg.model.IntygsStatus;

public interface WebcertRepositoryService {

    Intyg getIntygsUtkast(String intygsId);
    
    String getIntygsUtkastModel(String intygsId);
    
    IntygsStatus getIntygsUtkastStatus(String intygsId);
        
    boolean isIntygsUtkastPresent(String intygsId);
    
    boolean isVardenhetIntegrerad(String vardenhetHsaId);
    
    Long countNbrOfQuestionsForIntyg(String intygsId);
    
    Long countNbrOfAnsweredQuestionsForIntyg(String intygsId);
    
    Long countNbrOfHandledQuestionsForIntyg(String intygsId);
    
    Long countNbrOfHandledAndAnsweredQuestionsForIntyg(String intygsId);

}