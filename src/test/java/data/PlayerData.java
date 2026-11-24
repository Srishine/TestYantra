package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PlayerData(List<UserData> users) {
    public record UserData(
            @JsonProperty("EmailField") String email,
            @JsonProperty("NicknameField") String nickname,
            @JsonProperty("PasswordField") String password,
            @JsonProperty("DayField") String day,
            @JsonProperty("MonthField") String month,
            @JsonProperty("YearField") String year) {
    }
}