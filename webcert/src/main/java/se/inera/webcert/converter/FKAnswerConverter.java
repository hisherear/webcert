package se.inera.webcert.converter;

import se.inera.webcert.converter.util.ConvertToFKTypes;
import se.inera.webcert.medcertqa.v1.Amnetyp;
import se.inera.webcert.medcertqa.v1.KompletteringType;
import se.inera.webcert.medcertqa.v1.VardAdresseringsType;
import se.inera.webcert.persistence.fragasvar.model.FragaSvar;
import se.inera.webcert.persistence.fragasvar.model.Komplettering;
import se.inera.webcert.sendmedicalcertificateanswerresponder.v1.AnswerToFkType;

/**
 * Created by pehr on 10/2/13.
 */
public class FKAnswerConverter {

    public static AnswerToFkType convert(FragaSvar fs){
        AnswerToFkType fkAnswer = new AnswerToFkType();

        fkAnswer.setAmne(ConvertToFKTypes.toAmneTyp(fs.getAmne()));
        fkAnswer.setAdressVard(ConvertToFKTypes.toVardAdresseringsType(fs.getVardperson()));

        fkAnswer.setAvsantTidpunkt(fs.getFrageSkickadDatum());
        if (fs.getMeddelandeRubrik() != null) {
            fkAnswer.setFkMeddelanderubrik(fs.getMeddelandeRubrik());
        }

        fkAnswer.setFkReferensId(fs.getExternReferens());
        fkAnswer.setVardReferensId(fs.getInternReferens().toString());

        if(fs.getSistaDatumForSvar()!= null){
            fkAnswer.setFkSistaDatumForSvar(fs.getSistaDatumForSvar());
        }
        fkAnswer.setFraga(ConvertToFKTypes.toInnehallType(fs.getFrageText(), fs.getFrageSigneringsDatum()));
        fkAnswer.setSvar(ConvertToFKTypes.toInnehallType(fs.getSvarsText(), fs.getSvarSigneringsDatum()));

        fkAnswer.setLakarutlatande(ConvertToFKTypes.toLakarUtlatande(fs.getIntygsReferens()));

        if(fs.getKompletteringar()!=null){
            for (Komplettering komplettering: fs.getKompletteringar()) {
                KompletteringType kt = new KompletteringType();
                kt.setFalt(komplettering.getFalt());
                kt.setText(komplettering.getText());
                fkAnswer.getFkKomplettering().add(kt);
            }
        }
        return fkAnswer;
    }


}
