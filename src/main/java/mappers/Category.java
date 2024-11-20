package mappers;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    private long id;
    private String name;
    private String description;
    private boolean isActive;
    private String createdBy;
    private String lastModifiedBy;
    private List<GroupRoleDTO> userGroups;
    private List<Integer> applicationsIds;
    private Image imageBase64;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return isActive() == category.isActive() && Objects.equals(getName(), category.getName()) && Objects.equals(getDescription(), category.getDescription());
    }
}
