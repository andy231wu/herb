package au.com.new1step.herbalist.jpa.model;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

	@Column(name="Create_Date")
	private LocalDateTime createDate;
	@Column(name="Update_Date")
	private LocalDateTime updateDate;
	@Column(name="Create_User")
	private String createUser;
	@Column(name="Update_User")
	private String updateUser;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		createDate = now;
		updateDate = now;

		String username = "admin";
		if(auth != null) {
			username = auth.getName();
		}
		createUser = username;
		updateUser = username;
	}

	@PreUpdate
	public void preUpdate() {
		updateDate = LocalDateTime.now();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String username = "admin";
		if(auth != null){
			username = auth.getName();
		}
		updateUser = username;
	}

}

