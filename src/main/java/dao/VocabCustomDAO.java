package dao;

import request.VocabRequest;
import response.VocabResponse;

import java.util.ArrayList;

public class VocabCustomDAO {

    public VocabResponse getCustomVoab(VocabRequest vocabRequest){
        return new VocabResponse(new ArrayList<>(), true);
    }
}
