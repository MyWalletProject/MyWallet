package com.mywallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mywallet.domain.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long>{

	@Query(nativeQuery=true,value="Select count(action_id) from role_action where action_id=:actionId and role_id=:roleId")
	Integer getActionByActionIdAndRoleId(@Param ("actionId")Long actionId,@Param("roleId")Integer roleId);
	Action findByActionId(Long actionId);
	Action findByHandlerMethodName(String handlerMethodName);
}
