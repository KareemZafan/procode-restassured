package mappers;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mappers.helpers.GroupRoleDTO;
import mappers.helpers.Image;

import java.util.List;
import java.util.Objects;

@Data
@Builder(setterPrefix = "set")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    private long id;
    private String name;
    private String description;
    private String link;
    private long categoryId;
    private boolean isActive;
    private boolean isFavourite;
    private String createdBy;
    private String lastModifiedBy;
    private long categoryID;
    private List<GroupRoleDTO> userGroups;
    private List<GroupRoleDTO> roles;
    private List<String> assignedUsers;
    private Image imageBase64;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Application that)) return false;
        return isActive() == that.isActive() && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getLink(), that.getLink());
    }

}
