import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for store data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {

    public List<Document> documents = new ArrayList<>();

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null) {
            document.setId(generateId(document));
        }
        documents.add(document);
        return document;
    }

    private String generateId (Document document) {
        String id = Integer.toString(document.hashCode());
        boolean idUnique = true;
        do {
            if (!idUnique) {
                int newId = Integer.parseInt(id) + 1;
                id = Integer.toString(newId);
                idUnique = true;
            }
            for (Document doc : documents) {
                if (doc.getId().equals(id)) {
                    idUnique = false;
                }
            }
        } while (!idUnique);
        return id;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return documents.stream()
                .filter(object -> {
                    if (request.getTitlePrefixes() == null) return true;
                    for (String prefix : request.getTitlePrefixes()) {
                        if (object.getTitle().startsWith(prefix)) return true;
                    }
                    return false;
                })
                .filter(object -> request.getContainsContents() == null ? true :
                        request.getContainsContents().contains(object.getContent()))
                .filter(object -> request.getAuthorIds() == null ? true :
                        request.getAuthorIds().contains(object.getAuthor().getId()))
                .filter(object -> request.getCreatedFrom() == null ? true :
                        object.getCreated().isAfter(request.getCreatedFrom()))
                .filter(object -> request.getCreatedTo() == null ? true :
                        object.getCreated().isBefore(request.getCreatedTo()))
                .toList();
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return documents.stream().filter(object -> object.getId().equals(id)).findFirst();
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}