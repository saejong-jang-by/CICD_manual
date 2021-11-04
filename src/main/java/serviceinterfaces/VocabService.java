package serviceinterfaces;

import request.VocabRequest;
import response.VocabResponse;

import java.io.IOException;

public interface VocabService {
    VocabResponse getVocabs(VocabRequest vocabRequest) throws IOException;









}


