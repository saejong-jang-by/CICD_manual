package response;

import model.Vocab;

import java.util.List;

public class VocabResponse extends Response{

    private List<Vocab> vocabList;

    public VocabResponse(List<Vocab> vocabList, boolean success){
        super(success);
        this.vocabList = vocabList;
    }

    public VocabResponse(String message, boolean success){
        super(success, message);
    }

    public List<Vocab> getVocabList() {
        return vocabList;
    }

    public void setVocabList(List<Vocab> vocabList) {
        this.vocabList = vocabList;
    }
}

