package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long>{

	Action findByActionId(Long actionId);
	Action findByHandlerMethodName(String handlerMethodName);
}
