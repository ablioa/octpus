package org.fnd.modulator.rules.repository;

import org.fnd.modulator.rules.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, String> {
}
