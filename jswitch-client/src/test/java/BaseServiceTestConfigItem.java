import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseServiceTestConfigItem {

    private String name;

    public BaseServiceTestConfigItem() {
    }

    public BaseServiceTestConfigItem(String name) {
        this.name = name;
    }

}
