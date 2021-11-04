package service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.VocabRequest;
import response.VocabResponse;
import serviceinterfaces.VocabService;

import java.io.IOException;

public class VocabHandler implements RequestHandler<VocabRequest, VocabResponse> {

    @Override
    public VocabResponse handleRequest(VocabRequest vocabRequest, Context context) {
        VocabService vocabService = new VocabIpml();
        try {
            return vocabService.getVocabs(vocabRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VocabResponse("fail to get vocabularies", false);
    }
}


