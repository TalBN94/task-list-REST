package dtos;

import enums.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChoreDto extends TaskDto{
    private String description;
    private Size size;
    private final String type = Constants.CHORE;
}
