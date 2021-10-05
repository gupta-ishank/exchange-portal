package rwos.exchange.portal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwos.exchange.portal.Entity.AdminData;
import rwos.exchange.portal.Repository.AdminDataRepository;

@Service
public class AdminDataService {
	@Autowired
	AdminDataRepository adminDataRepository;
	
	public String addAdminData(AdminData adminData) {
		adminDataRepository.save(adminData);
		return "successfully added data";
	}
	
	public String updateAdminData(AdminData adminData) {
		AdminData adminDataFromDb = adminDataRepository.findById(adminData.getFilePath());
		adminDataFromDb.setFilePath(adminData.getFilePath());
		adminDataFromDb.setRole(adminData.getRole());
		adminDataRepository.save(adminDataFromDb);
		return "successfully updated";
	}
}
