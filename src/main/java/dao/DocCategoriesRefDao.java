package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import config.AppConfig;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;

public class DocCategoriesRefDao extends AbstractDocDBDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDBClient.class);

    private static final String DOC_CATEGORY = "doc_category";
    private static final String DOC_CATEGORY_DISPLAY = "doc_category_display";
    private static final String DOC_CATEGORY_FIELDS = "doc_fields";

    private MongoCollection<Document> docCategoriesCollection;

    public DocCategoriesRefDao() {
        super(Connection.getInstance().getClient(), AppConfig.DB_NAME.getValue());
        this.docCategoriesCollection = database.getCollection(AppConfig.COLL_DOC_CATEGORIES_REF.getValue());
    }

    /**
     * Find all document categories available
     * @return List of Document objects.
     */
    public List<Document> getDocCategories() {
        List<Document> docCategories = new ArrayList<>();

        docCategoriesCollection.find()
                .projection(fields(include(DOC_CATEGORY, DOC_CATEGORY_DISPLAY), excludeId()))
                .sort(Sorts.ascending(DOC_CATEGORY))
                .into(docCategories);

        return docCategories;
    }

    //TODO - Try to implement find with Index
    public List<Document> getDocCategoriesByIndex() {
        List<Document> docCategories = new ArrayList<>();
        docCategoriesCollection.find()
                .projection(fields(include(DOC_CATEGORY, DOC_CATEGORY_DISPLAY), excludeId()))
                .sort(Sorts.ascending(DOC_CATEGORY))
                .into(docCategories);
        return docCategories;
    }

    /**
     * For a given a document category, return the document category details.
     *
     * @param docCategory - document category string value to be matched.
     * @return Document object for matching document category.
     */
    public Document getDocCategoryFields(String docCategory) {
        LOGGER.info("Getting Document Category fields for - " + docCategory);
        return docCategoriesCollection.find(eq(DOC_CATEGORY, docCategory))
                .projection(fields(include(DOC_CATEGORY, DOC_CATEGORY_DISPLAY, DOC_CATEGORY_FIELDS), excludeId()))
                .first();
    }

}
