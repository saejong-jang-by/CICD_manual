package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import model.Vocab;
import request.VocabRequest;
import response.VocabResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.amazonaws.regions.Regions.US_WEST_2;

public class VocabDAO {

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(US_WEST_2).build();
    private static DynamoDB db = new DynamoDB(client);
    private static final String TABLE_NAME = "Vocab";
    private static final String USER_ALIAS = "alias";
    private static final String VOCAB_VALUE = "value";
    private static final String ALIAS_KEY = ":alias";
    private static final String ALIAS_KEY_CONDITION_EXP = "alias = " + ALIAS_KEY;

    public ItemCollection<QueryOutcome> getVocabLists(int requestedAlias){
        Table table = db.getTable(TABLE_NAME);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(ALIAS_KEY, requestedAlias);

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression(ALIAS_KEY_CONDITION_EXP)
                .withValueMap(valueMap)
                .withScanIndexForward(false);

        ItemCollection<QueryOutcome> items = null;

        try {
            items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            iterator.hasNext();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return items;
    }



    public VocabResponse getVocabs(VocabRequest vocabRequest){

        Table table = db.getTable(TABLE_NAME);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":alias", vocabRequest.getUserID());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("alias = :alias")
                .withValueMap(valueMap)
                .withScanIndexForward(false);

        ItemCollection<QueryOutcome> items = null;

        try {
            items = table.query(querySpec);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        QueryOutcome outcome = items.getLastLowLevelResult();

        List<Vocab> vocabList = new ArrayList<>();
        for(Item item : outcome.getItems()){
            Vocab vocab = new Vocab(item.getString(VOCAB_VALUE));
            vocabList.add(vocab);
        }

        return new VocabResponse(vocabList, true);
    }
}
