package rwos.exchange.portal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwos.exchange.portal.Entity.Role;
import rwos.exchange.portal.Repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role addRole(String role){
        return roleRepository.save(new Role(role));
    }

    public Role getRoleByRoleName(String roleName){
        return roleRepository.findByName(roleName);
    }
}
