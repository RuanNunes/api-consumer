package picpay.com.consumer.domain;
import org.bson.codecs.pojo.annotations.BsonProperty;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MongoEntity(collection = "consumers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Consumer extends PanacheMongoEntity{
    @BsonProperty("legacy_id")
    private String legacyId;
    private String name;
    @BsonProperty("user_name")
    private String userName;
    private String email;
    private String cpf;
    @BsonProperty("phone_number")
    private String phoneNumber;
    @BsonProperty("imagem_url")
    private String imagemUrl;

    public void updateEntity(final Consumer newConsumer){
        this.email = newConsumer.getEmail();
        this.imagemUrl = newConsumer.getImagemUrl();
        this.name = newConsumer.getName();
        this.userName = newConsumer.getUserName();
        this.phoneNumber = newConsumer.getPhoneNumber();
    }

    public static Consumer findByLegacyId(final String legacyId){
        return find("legacy_id", legacyId).firstResult();
    }

	public static boolean exists(final Consumer entity) {
		return findByLegacyId(entity.getLegacyId()) != null;
	}
    
}
