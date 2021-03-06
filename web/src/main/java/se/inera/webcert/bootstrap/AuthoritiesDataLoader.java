package se.inera.webcert.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.inera.webcert.common.security.authority.UserPrivilege;
import se.inera.webcert.common.security.authority.UserRole;
import se.inera.webcert.persistence.roles.model.Privilege;
import se.inera.webcert.persistence.roles.model.Role;
import se.inera.webcert.persistence.roles.model.TitleCode;
import se.inera.webcert.persistence.roles.repository.PrivilegeRepository;
import se.inera.webcert.persistence.roles.repository.RoleRepository;
import se.inera.webcert.persistence.roles.repository.TitleCodeRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Magnus Ekstrand on 28/08/15.
 */
@Component
public class AuthoritiesDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthoritiesDataLoader.class);

    private boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private TitleCodeRepository titleCodeRepository;


    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create privileges
        loadPrivileges(UserPrivilege.values());

        // == create roles
        loadRoles(UserRole.values());

        // == create roles
       loadTitleCodes();

        alreadySetup = true;
    }


    // - - - - - Private scope - - - - -

    private void loadPrivileges(UserPrivilege[] userPrivileges) {

        LOG.debug("Loading user privileges");

        if (userPrivileges == null) {
            throw new IllegalArgumentException("User privileges cannot be null");
        }

        for (UserPrivilege up : userPrivileges) {
            createPrivilegeIfNotFound(up);
        }
    }

    private void loadRoles(UserRole[] userRoles) {

        LOG.debug("Loading user roles");

        if (userRoles == null) {
            throw new IllegalArgumentException("User roles cannot be null");
        }

        Map<UserRole, List<UserPrivilege>> userRolesUserPrivilegesMap = getUserRolesPrivilegesMap();

        for (UserRole userRole : userRoles) {
            if (userRolesUserPrivilegesMap.containsKey(userRole)) {
                createRoleIfNotFound(userRole, getPrivilegeList(userRolesUserPrivilegesMap.get(userRole)));
            } else {
                LOG.warn("User role {} has not been setup with any privileges. Role will not be created.", userRole.name());
            }
        }
    }

    private void loadTitleCodes() {

        LOG.debug("Loading title codes and group prescription codes");

        String[][] titleCodesMatrix = getTitleCodesMatrix();

        for (String[] sarr : titleCodesMatrix) {
            createTitleCodeIfNotFound(sarr[0], sarr[1], UserRole.valueOf(sarr[2]));
        }
    }

    private Privilege createPrivilegeIfNotFound(final UserPrivilege userPrivilege) {
        Privilege privilege = privilegeRepository.findByName(userPrivilege.name());
        if (privilege == null) {
            privilege = new Privilege(userPrivilege.name(), userPrivilege.toString());
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role createRoleIfNotFound(final UserRole userRole, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(userRole.name());
        if (role == null) {
            role = new Role(userRole.name(), userRole.toString());
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    private TitleCode createTitleCodeIfNotFound(final String titleCode, final String groupPrescriptionCode, UserRole userRole) {
        Role role = roleRepository.findByName(userRole.name());
        if (role == null) {
            LOG.error("Could not create TitleCode {} since user role {} was not present in database.", titleCode, userRole.name());
            return null;
        }

        //List<TitleCode> titleCodes = titleCodeRepository.findByTitleCode(titleCode);
        TitleCode tc = titleCodeRepository.findByTitleCodeAndGroupPrescriptionCode(titleCode, groupPrescriptionCode);
        if (tc == null) {
            tc = new TitleCode();
            tc.setTitleCode(titleCode);
            tc.setGroupPrescriptionCode(groupPrescriptionCode);
            tc.setRole(role);
            titleCodeRepository.save(tc);
        }

        return tc;
    }

    private Set<Privilege> getPrivilegeList(final List<UserPrivilege> userPrivileges) {

        Set<Privilege> privileges = new HashSet<>();

        for (UserPrivilege userPrivilege: userPrivileges) {
            Privilege privilege = privilegeRepository.findByName(userPrivilege.name());
            if (privilege == null) {
                privilege = new Privilege(userPrivilege.name(), userPrivilege.toString());
            }
            privileges.add(privilege);
        }

        return privileges;
    }


    // ~ User and privileges mapping
    // ======================================================================================================
    //
    // TODO externalize mapping section
    //
    private String[][] getTitleCodesMatrix() {
        String [][] matrix = {
            {"204010 ", "0000000", UserRole.ROLE_LAKARE.name()},
            {"203090 ", "9300005", UserRole.ROLE_LAKARE.name()},
            {"203090 ", "9400003", UserRole.ROLE_LAKARE.name()},
            {"204090 ", "9100009", UserRole.ROLE_LAKARE.name()}
        };
        return matrix;
    }

    private Map<UserRole, List<UserPrivilege>> getUserRolesPrivilegesMap() {

        Map<UserRole, List<UserPrivilege>> map = new HashMap<>();

        map.put(UserRole.ROLE_LAKARE, getPrivileges(UserRole.ROLE_LAKARE));
        map.put(UserRole.ROLE_LAKARE_DJUPINTEGRERAD, getPrivileges(UserRole.ROLE_LAKARE_DJUPINTEGRERAD));
        map.put(UserRole.ROLE_LAKARE_UTHOPP, getPrivileges(UserRole.ROLE_LAKARE_UTHOPP));
        map.put(UserRole.ROLE_PRIVATLAKARE, getPrivileges(UserRole.ROLE_PRIVATLAKARE));
        map.put(UserRole.ROLE_TANDLAKARE, getPrivileges(UserRole.ROLE_TANDLAKARE));
        map.put(UserRole.ROLE_VARDADMINISTRATOR, getPrivileges(UserRole.ROLE_VARDADMINISTRATOR));
        map.put(UserRole.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD, getPrivileges(UserRole.ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD));
        map.put(UserRole.ROLE_VARDADMINISTRATOR_UTHOPP, getPrivileges(UserRole.ROLE_VARDADMINISTRATOR_UTHOPP));

        return map;
    }

    private List<UserPrivilege> getPrivileges(UserRole userRoles) {
        List<UserPrivilege> userPrivileges = null;

        switch (userRoles) {
            case ROLE_LAKARE:
                userPrivileges = getLakarePrivilegeList();
                break;
            case ROLE_LAKARE_DJUPINTEGRERAD:
                userPrivileges = getDjupintegreradLakarePrivilegeList();
                break;
            case ROLE_LAKARE_UTHOPP:
                userPrivileges = getUthoppsLakarePrivilegeList();
                break;
            case ROLE_PRIVATLAKARE:
                userPrivileges = getPrivatLakarePrivilegeList();
                break;
            case ROLE_TANDLAKARE:
                userPrivileges = getTandlakarePrivilegeList();
                break;
            case ROLE_VARDADMINISTRATOR:
                userPrivileges = getVardadministratorPrivilegeList();
                break;
            case ROLE_VARDADMINISTRATOR_DJUPINTEGRERAD:
                userPrivileges = getDjupintegreradVardadministratorPrivilegeList();
                break;
            case ROLE_VARDADMINISTRATOR_UTHOPP:
                userPrivileges = getUthoppsVardadministratorPrivilegeList();
                break;
            default:
                // Return empty list if
                userPrivileges = new ArrayList<UserPrivilege>();
        }

        return userPrivileges;
    }

    private List<UserPrivilege> getVardadministratorPrivilegeList() {
        return Arrays.asList(new UserPrivilege[] {
            UserPrivilege.PRIVILEGE_SKRIVA_INTYG,
            UserPrivilege.PRIVILEGE_KOPIERA_INTYG,
            UserPrivilege.PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR,
            UserPrivilege.PRIVILEGE_VIDAREBEFORDRA_UTKAST });
    }

    private List<UserPrivilege> getUthoppsVardadministratorPrivilegeList() {
        return Arrays.asList(new UserPrivilege[] {
                UserPrivilege.PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR,
                UserPrivilege.PRIVILEGE_VIDAREBEFORDRA_UTKAST });
    }

    private List<UserPrivilege> getDjupintegreradVardadministratorPrivilegeList() {
        return Arrays.asList(new UserPrivilege[] {
                UserPrivilege.PRIVILEGE_SKRIVA_INTYG,
                UserPrivilege.PRIVILEGE_KOPIERA_INTYG });
    }

    private List<UserPrivilege> getTandlakarePrivilegeList() {
        // TODO ordna med rättigheter för tandläkare. Det mesta talar för att tandläkare skall ha exakt samma som
        // vanlig läkare, men enbart för fk7263
        return Arrays.asList(UserPrivilege.values());
    }

    private List<UserPrivilege> getPrivatLakarePrivilegeList() {
        return Arrays.asList(new UserPrivilege[] {
                UserPrivilege.PRIVILEGE_SKRIVA_INTYG,
                UserPrivilege.PRIVILEGE_KOPIERA_INTYG,
                UserPrivilege.PRIVILEGE_MAKULERA_INTYG,
                UserPrivilege.PRIVILEGE_SIGNERA_INTYG,
                UserPrivilege.PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA });
    }

    private List<UserPrivilege> getUthoppsLakarePrivilegeList() {
        return Arrays.asList(new UserPrivilege[] {
                UserPrivilege.PRIVILEGE_SIGNERA_INTYG,
                UserPrivilege.PRIVILEGE_VIDAREBEFORDRA_UTKAST,
                UserPrivilege.PRIVILEGE_VIDAREBEFORDRA_FRAGASVAR,
                UserPrivilege.PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA });
    }

    private List<UserPrivilege> getDjupintegreradLakarePrivilegeList() {
        return Arrays.asList(new UserPrivilege[] {
                UserPrivilege.PRIVILEGE_SKRIVA_INTYG,
                UserPrivilege.PRIVILEGE_KOPIERA_INTYG,
                UserPrivilege.PRIVILEGE_MAKULERA_INTYG,
                UserPrivilege.PRIVILEGE_SIGNERA_INTYG,
                UserPrivilege.PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA });
    }

    private List<UserPrivilege> getLakarePrivilegeList() {
        return Arrays.asList(UserPrivilege.values());
    }

}

