package se.inera.webcert.service.diagnos.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.webcert.service.diagnos.model.Diagnos;

public class DiagnosRepositoryTest {
    
    private static DiagnosRepository repo;
    
    private static final String FILE_1 = "/DiagnosService/KSH97_TESTKODER_1.ANS";
    
    @BeforeClass
    public static void setup() {
        DiagnosRepositoryFactory factory = new DiagnosRepositoryFactory(Arrays.asList(FILE_1));
        DiagnosRepositoryImpl repoImpl = (DiagnosRepositoryImpl) factory.createAndInitDiagnosRepository();
        assertEquals(100, repoImpl.nbrOfDiagosis());
        repo = repoImpl;
    }
    
    @Test
    public void testGetByCodeWithNullAndEmpty() {
        Diagnos res = repo.getDiagnosByCode(null);
        assertNull(res);
        
        res = repo.getDiagnosByCode("");
        assertNull(res);
    }
    
    @Test
    public void testGetByCode() {
        String code = "A051";
        Diagnos res = repo.getDiagnosByCode(code);
        assertEquals("A051", res.getKod());
        assertEquals("Botulism", res.getBeskrivning());
    }
    
    @Test
    public void testGetByCodeCheckEncoding() {
        String code = "A062";
        Diagnos res = repo.getDiagnosByCode(code);
        assertEquals("A062", res.getKod());
        assertEquals("Kolit orsakad av amöba", res.getBeskrivning());
    }
    
    @Test
    public void testGetByCodeWithMalformedCode() {
        String code = " a 051  ";
        Diagnos res = repo.getDiagnosByCode(code);
        assertEquals("A051", res.getKod());
        assertEquals("Botulism", res.getBeskrivning());
    }
    
    @Test
    public void testSearchingWithFragmentOne() {
        
        String codeFragment = "A";
        List<Diagnos> res = repo.searchDiagnosisByCode(codeFragment);
        assertEquals(100, res.size());
    }
    
    @Test
    public void testSearchingWithFragmentTwo() {
        
        String codeFragment = "A0";
        List<Diagnos> res = repo.searchDiagnosisByCode(codeFragment);
        assertEquals(63, res.size());
    }
    
    @Test
    public void testSearchingWithFragmentThree() {
        
        String codeFragment = "A07";
        List<Diagnos> res = repo.searchDiagnosisByCode(codeFragment);
        assertEquals(6, res.size());
    }
    
    @Test
    public void testSearchingWithFullCode() {
        
        String codeFragment = "A083B";
        List<Diagnos> res = repo.searchDiagnosisByCode(codeFragment);
        assertEquals(1, res.size());
    }
        
    @Test
    public void testSearchingWithNonExistingFragment() {
        
        String codeFragment = "B1";
        List<Diagnos> res = repo.searchDiagnosisByCode(codeFragment);
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }
    
    @Test
    public void testSearchingWithNoInput() {
        
        String codeFragment = "";
        List<Diagnos> res = repo.searchDiagnosisByCode(codeFragment);
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }
    
    @Test
    public void testSearchingWithNull() {
        
        String codeFragment = null;
        List<Diagnos> res = repo.searchDiagnosisByCode(codeFragment);
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }
}
