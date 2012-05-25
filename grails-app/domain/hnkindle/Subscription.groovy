package hnkindle



import javax.persistence.*
import com.google.appengine.api.datastore.Text;
// import com.google.appengine.api.datastore.Key;

@Entity
class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id
    
    @Basic
    String email

    @Basic
    boolean subscribed

    static constraints = {
        id visible: false
    }
}
