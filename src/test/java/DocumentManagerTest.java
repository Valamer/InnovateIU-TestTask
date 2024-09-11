import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentManagerTest {

    @Test
    public void searchTest1() {
        DocumentManager documentManager = new DocumentManager();
        DocumentManager.Document expected = DocumentManager.Document.builder()
                .id("id")
                .title("Spring in Action")
                .content("Some random content")
                .author(DocumentManager.Author.builder()
                        .id("2")
                        .name("Craig Walls")
                        .build())
                .created(null)
                .build();
        DocumentManager.Document unfit = DocumentManager.Document.builder()
                .id("id")
                .title("Framework Spring")
                .content("Some random content")
                .author(DocumentManager.Author.builder()
                        .id("2")
                        .name("Craig Walls")
                        .build())
                .created(null)
                .build();
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Python","Spring","Content"))
                .containsContents(null)
                .authorIds(null)
                .createdFrom(null)
                .createdTo(null)
                .build();
        documentManager.save(expected);
        documentManager.save(unfit);
        assertEquals(List.of(expected), documentManager.search(request));
    }

    @Test
    public void searchTest2() {
        DocumentManager documentManager = new DocumentManager();
        DocumentManager.Document expected = DocumentManager.Document.builder()
                .id("id")
                .title("Spring in Action")
                .content("Some random content")
                .author(DocumentManager.Author.builder()
                        .id("2")
                        .name("Craig Walls")
                        .build())
                .created(null)
                .build();
        DocumentManager.Document unfit = DocumentManager.Document.builder()
                .id("different id")
                .title("Framework Spring")
                .content("Another random content")
                .author(DocumentManager.Author.builder()
                        .id("3")
                        .name("Bread")
                        .build())
                .created(null)
                .build();
        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Python","Spring","Content"))
                .containsContents(List.of("Some random content","Another random content"))
                .authorIds(List.of("2","5","6"))
                .createdFrom(null)
                .createdTo(null)
                .build();
        documentManager.save(expected);
        documentManager.save(unfit);
        assertEquals(List.of(expected), documentManager.search(request));
    }


    @Test
    public void findByIdTest() {
        DocumentManager documentManager = new DocumentManager();
        DocumentManager.Document document = DocumentManager.Document.builder()
                .id("id")
                .title("Spring in Action")
                .content("Some random content")
                .author(DocumentManager.Author.builder()
                        .id("2")
                        .name("Craig Walls")
                        .build())
                .created(null)
                .build();
        documentManager.save(document);
        assertEquals(document, documentManager.findById("id").get());
    }
}
