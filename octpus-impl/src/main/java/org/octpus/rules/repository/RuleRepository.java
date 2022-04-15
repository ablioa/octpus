package org.octpus.rules.repository;

import org.octpus.rules.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, String> {
}
