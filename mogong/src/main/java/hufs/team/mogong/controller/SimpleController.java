package hufs.team.mogong.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SimpleController {

	@GetMapping("/simple")
	public ResponseEntity<Void> simpleRead() {
		return ResponseEntity.ok().build();
	}
}
