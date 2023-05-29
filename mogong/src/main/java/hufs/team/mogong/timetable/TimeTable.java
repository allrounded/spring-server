package hufs.team.mogong.timetable;

import hufs.team.mogong.member.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TimeTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(nullable = false)
	protected Long mon;

	@Column(nullable = false)
	protected Long tue;

	@Column(nullable = false)
	protected Long wed;

	@Column(nullable = false)
	protected Long thu;

	@Column(nullable = false)
	protected Long fri;

	@Column(nullable = false)
	protected Long sat;

	@Column(nullable = false)
	protected Long sun;

	public void update(List<Long> times) {
		this.mon = times.get(0);
		this.tue = times.get(1);
		this.wed = times.get(2);
		this.thu = times.get(3);
		this.fri = times.get(4);
		this.sat = times.get(5);
		this.sun = times.get(6);
	}

	public void accumulate(long[] times) {
		times[0] |= this.mon;
		times[1] |= this.tue;
		times[2] |= this.wed;
		times[3] |= this.thu;
		times[4] |= this.fri;
		times[5] |= this.sat;
		times[6] |= this.sun;
	}
}
