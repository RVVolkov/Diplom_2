import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IngredientsResponsePojo {
    @Getter
    @Setter
    public class Data {
        @SerializedName("_id")
        private String id;
        private String name;
        private String type;
        private int proteins;
        private int fat;
        private int carbohydrates;
        private int calories;
        private int price;
        private String image;
        private String image_mobile;
        private String image_large;
        @SerializedName("__v")
        private int v;
    }

    private boolean success;
    private List<Data> data;
}
