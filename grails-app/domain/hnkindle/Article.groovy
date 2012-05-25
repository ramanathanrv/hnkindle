package hnkindle



import javax.persistence.*
import com.google.appengine.api.datastore.Text;
// import com.google.appengine.api.datastore.Key;

@Entity
class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Basic
    String link

    @Basic
    String title

    Date dateCreated
    Date lastModified

    @Basic
    String state //can be one of NEW, FETCHED, EXTRACTED, SENT

    Text html;
    Text extractedContent; //we will be sending this to kindle

    static constraints = {
        id visible: false
    }
}
