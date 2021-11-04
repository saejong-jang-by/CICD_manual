package service;

import dao.VocabDAO;
import request.VocabRequest;
import response.VocabResponse;
import serviceinterfaces.VocabService;

public class VocabIpml implements VocabService {

    @Override
    public VocabResponse getVocabs(VocabRequest vocabRequest) {
        VocabDAO vocabDAO = new VocabDAO();
        return vocabDAO.getVocabs(vocabRequest);
    }









}
