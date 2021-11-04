package service;

import dao.VocabCustomDAO;
import dao.VocabDAO;
import request.VocabRequest;
import response.VocabResponse;
import serviceinterfaces.VocabService;

import java.io.IOException;

public class VocabCustomImpl implements VocabService {

    @Override
    public VocabResponse getVocabs(VocabRequest vocabRequest) throws IOException {
        VocabDAO vocabDAO = new VocabDAO();
        VocabCustomDAO vocabCustomDAO = new VocabCustomDAO();
        return vocabCustomDAO.getCustomVoab(vocabRequest);
    }







}
