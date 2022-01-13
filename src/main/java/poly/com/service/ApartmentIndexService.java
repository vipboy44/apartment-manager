package poly.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import poly.com.constant.ElectricityLimits;
import poly.com.constant.MessageError;
import poly.com.constant.MessageSuccess;
import poly.com.constant.NameTypeVehicel;
import poly.com.dto.ApartmentBillDTO;
import poly.com.dto.ResponseDTO;
import poly.com.entity.ApartmentIndex;
import poly.com.entity.Bill;
import poly.com.entity.Employee;
import poly.com.entity.PriceElectricity;
import poly.com.entity.PriceGarbage;
import poly.com.entity.PriceManagement;
import poly.com.entity.PriceParking;
import poly.com.entity.PriceWater;
import poly.com.entity.TypeVehicel;
import poly.com.repository.ApartmentIndexRepository;
import poly.com.repository.ApartmentRepository;
import poly.com.repository.BillRepository;
import poly.com.repository.EmployeeRepository;
import poly.com.repository.PriceElectricityRepository;
import poly.com.repository.PriceGarbageRepository;
import poly.com.repository.PriceManagementRepository;
import poly.com.repository.PriceParkingRepository;
import poly.com.repository.PriceWaterRepository;
import poly.com.repository.TypeVehicelRepository;
import poly.com.repository.VehicleRespository;
import poly.com.request.CreateIndexRequest;
import poly.com.request.UpdateIndexRequest;

@Service
public class ApartmentIndexService {

	@Autowired
	ApartmentIndexRepository apartmentIndexRepository;
	
	@Autowired
	ApartmentRepository apartmentRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	BillRepository billRepository;

	@Autowired
	PriceWaterRepository priceWaterRepository;

	@Autowired
	PriceManagementRepository priceManagementRepository;

	@Autowired
	PriceGarbageRepository priceGarbageRepository;

	@Autowired
	PriceElectricityRepository priceElectricityRepository;

	@Autowired
	PriceParkingRepository priceParkingRepository;

	@Autowired
	VehicleRespository vehicleRespository;
	
	@Autowired
	TypeVehicelRepository typeVehicelRepository;
	

	public ResponseEntity<ResponseDTO> findAll() {
		try {
			List<Bill> lists = billRepository.findAll();
			List<ApartmentBillDTO> listDTOs = new ArrayList<>();
			for (Bill bill : lists) {
				ApartmentBillDTO dto = new ApartmentBillDTO(bill.getId(),
						bill.getApartmentIndex().getApartment().getId(), bill.getApartmentIndex().getDate(),
						bill.getTotalPrice(), bill.getApartmentIndex().getEmployee().getUsername(), bill.getPaid());
				listDTOs.add(dto);
			}

			return ResponseEntity.ok(new ResponseDTO(listDTOs, null));
		} catch (Exception e) {

			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	public ResponseEntity<ResponseDTO> findByPaid(Boolean paid) {
		try {
			List<Bill> lists = billRepository.findByPaid(paid);
			List<ApartmentBillDTO> listDTOs = new ArrayList<>();
			for (Bill bill : lists) {
				ApartmentBillDTO dto = new ApartmentBillDTO(bill.getId(),
						bill.getApartmentIndex().getApartment().getId(), bill.getApartmentIndex().getDate(),
						bill.getTotalPrice(), bill.getApartmentIndex().getEmployee().getUsername(), bill.getPaid());
				listDTOs.add(dto);
			}

			return ResponseEntity.ok(new ResponseDTO(listDTOs, null));
		} catch (Exception e) {

			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	public ResponseEntity<ResponseDTO> findById(int id) {
		try {
			Bill bill = billRepository.findById(id).orElse(null);
			
			return ResponseEntity.ok(new ResponseDTO(bill, null));
		} catch (Exception e) {

			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@SuppressWarnings("deprecation")
	public ResponseEntity<ResponseDTO> create(CreateIndexRequest request) {
		int waterNumber = request.getWarterNumber() ;
		int electricityNumber = request.getElectricityNumber();	
		try {	
				
			if (!apartmentRepository.existsById(request.getApartment().getId())) 
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_APARTMENT), HttpStatus.NOT_FOUND);
			
		
			ApartmentIndex apartmentIndex = apartmentIndexRepository.findByApartmentAndYearAndMonth(request.getApartment(),
																	 request.getDate().getYear() + 1900,
																	 request.getDate().getMonth()+ 1).orElse(null);
			
			if (apartmentIndex != null) 
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_APARTMENT_INDEX), HttpStatus.CONFLICT);

			apartmentIndex = new ApartmentIndex(null, request.getElectricityNumber(),
							request.getWarterNumber(), null, null, null, request.getDate(), request.getApartment(),
							request.getEmployee());
				
	
			// Get price paking
			TypeVehicel bicycleType =  typeVehicelRepository.findByName(NameTypeVehicel.BICYCLE).orElse(null);
			if (bicycleType == null) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_BICYCEL), HttpStatus.NOT_FOUND);
			}else {	
				apartmentIndex.setBicycleNumber(vehicleRespository.findTotalVehicelByType(request.getApartment().getId(), bicycleType.getId()));
			}
			PriceParking bicyclePrice =  priceParkingRepository.findFirstByDateLessThanEqualAndTypeVehicelOrderByDateDesc(
																apartmentIndex.getDate(),bicycleType ).orElse(null);
			if (bicyclePrice == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_BICYCEL), HttpStatus.NOT_FOUND);
			

			TypeVehicel motocycleType =  typeVehicelRepository.findByName(NameTypeVehicel.MOTOCYCLE).orElse(null);
			if (motocycleType == null) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_MOTOCYCLE), HttpStatus.NOT_FOUND);
			}else {	
				apartmentIndex.setMotocycleNumber(vehicleRespository.findTotalVehicelByType(request.getApartment().getId(), motocycleType.getId()));
			}
			PriceParking motocyclePrice =  priceParkingRepository.findFirstByDateLessThanEqualAndTypeVehicelOrderByDateDesc(
								 apartmentIndex.getDate(),motocycleType ).orElse(null);
			if (motocyclePrice == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_MOTOCYCEL), HttpStatus.NOT_FOUND);
			
			
			TypeVehicel carType =  typeVehicelRepository.findByName(NameTypeVehicel.CAR).orElse(null);
			if (carType == null) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_CAR), HttpStatus.NOT_FOUND);
			}else {	
				apartmentIndex.setCarNumber(vehicleRespository.findTotalVehicelByType(request.getApartment().getId(), carType.getId()));
			}
			PriceParking carPrice =  priceParkingRepository.findFirstByDateLessThanEqualAndTypeVehicelOrderByDateDesc(
									apartmentIndex.getDate(),carType ).orElse(null);
			if (carPrice == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_CAR), HttpStatus.NOT_FOUND);
          
			
			// Get price water		
			PriceWater priceWater = priceWaterRepository.findFirstByDateLessThanEqualOrderByDateDesc(apartmentIndex.getDate()).orElse(null);
			if (priceWater == null) 
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_WATER), HttpStatus.NOT_FOUND);
				
			PriceManagement managementPrice = priceManagementRepository.findFirstByDateLessThanEqualOrderByDateDesc(apartmentIndex.getDate())
																	   .orElse(null);
			if (managementPrice == null)
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_MANAGEMENT), HttpStatus.NOT_FOUND);
			
			PriceGarbage priceGarbage = priceGarbageRepository.findFirstByDateLessThanEqualOrderByDateDesc(apartmentIndex.getDate())
																		.orElse(null);
			if (priceGarbage == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_GARBAGE), HttpStatus.NOT_FOUND);
						
			PriceElectricity electricityPrice_1 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																	 apartmentIndex.getDate(),ElectricityLimits.LIMIT_1)
																	 .orElse(null);
			if (electricityPrice_1 == null)
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_1), HttpStatus.NOT_FOUND);
			
			PriceElectricity electricityPrice_2 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 apartmentIndex.getDate(),ElectricityLimits.LIMIT_2)
																			 .orElse(null);
			if (electricityPrice_2 == null)
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_2), HttpStatus.NOT_FOUND);
			
			PriceElectricity electricityPrice_3 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 apartmentIndex.getDate(),ElectricityLimits.LIMIT_3)
																			 .orElse(null);
			if (electricityPrice_3 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_3), HttpStatus.NOT_FOUND);
			
			PriceElectricity electricityPrice_4 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 apartmentIndex.getDate(),ElectricityLimits.LIMIT_4)
																			 .orElse(null);
			if (electricityPrice_4 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_4), HttpStatus.NOT_FOUND);
						
			PriceElectricity electricityPrice_5 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 apartmentIndex.getDate(),ElectricityLimits.LIMIT_5)
																			 .orElse(null);
			if (electricityPrice_5 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_5), HttpStatus.NOT_FOUND);
						
			PriceElectricity electricityPrice_6 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
					 apartmentIndex.getDate(),ElectricityLimits.LIMIT_6)
					 .orElse(null);
			if (electricityPrice_6 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_6), HttpStatus.NOT_FOUND);
			
	
			// Kiểm tra căn hộ đã từng tồn tại hóa đơn nào chưa -> set lại số điện nươc (default = 0 )
					
			int month = request.getDate().getMonth();
			int year  = request.getDate().getYear() + 1900;
			
			if (request.getDate().getMonth() == 0) {
				month = 12;
				year = year -1;
			}
				
			if (apartmentIndexRepository.existsByApartment(request.getApartment())) {
				ApartmentIndex apartmentIndexOld  = apartmentIndexRepository.findByMonthInYear(request.getApartment().getId(), year, month).orElse(null);
								
				if (apartmentIndexOld == null) {
					return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_OLD_APARTMENT_INDEX), HttpStatus.NOT_FOUND);
				}
				
				if (apartmentIndex.getNewElectricityNumber() < apartmentIndexOld.getNewElectricityNumber() ) {
					return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_LECTRICITY_NUMBER), HttpStatus.CONFLICT);
				}
				if (apartmentIndex.getNewWaterNumber()  < apartmentIndexOld.getNewWaterNumber() ) {
					return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_WATER_NUMBER ), HttpStatus.CONFLICT);
				}
				
				waterNumber = request.getWarterNumber() - apartmentIndexOld.getNewWaterNumber();
				electricityNumber = request.getElectricityNumber() - apartmentIndexOld.getNewElectricityNumber();	

			}
				
			// Insert into table ApartmentIndex
			apartmentIndex = apartmentIndexRepository.save(apartmentIndex);
		
		   // Insert into table Bills							
			Bill bill = new Bill(apartmentIndex.getId(), electricityNumber, electricityPrice_1.getPrice(), electricityPrice_2.getPrice(), electricityPrice_3.getPrice(),
					electricityPrice_4.getPrice(), electricityPrice_5.getPrice(), electricityPrice_6.getPrice(), 0.0, waterNumber, priceWater.getPrice(),
					apartmentIndex.getBicycleNumber(), bicyclePrice.getPrice(), apartmentIndex.getMotocycleNumber(), motocyclePrice.getPrice(),
					apartmentIndex.getCarNumber(), carPrice.getPrice(), 0.0, managementPrice.getPrice(),priceGarbage.getPrice(), 0,
					false, apartmentIndex);
			
			bill.setElectricityPriceTotal(electricityPriceTotal(bill));
			bill.setParkingPriceTotal(parkingPriceTotal(bill));
			bill.setTotalPrice(calculatorTotal(bill));	
			bill =  billRepository.save(bill);
			
			ApartmentBillDTO dto = new ApartmentBillDTO(bill.getId(),
					bill.getApartmentIndex().getApartment().getId(), bill.getApartmentIndex().getDate(),
					bill.getTotalPrice(), employeeRepository.findUserNameById(bill.getApartmentIndex().getEmployee().getId()), bill.getPaid());
			
			return ResponseEntity.ok(new ResponseDTO(dto, MessageSuccess.INSERT_SUCCSESS));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
	}

		
	@SuppressWarnings("deprecation")
	public ResponseEntity<ResponseDTO> update(int id, UpdateIndexRequest request) {
		int waterNumber = request.getWarterNumber() ;
		int electricityNumber = request.getElectricityNumber();	
		int month = request.getDate().getMonth();
		int year  = request.getDate().getYear() + 1900;
		
		try {	
			// Check exist apartmentIndex
			ApartmentIndex apartmentIndexOld =  apartmentIndexRepository.findById(id).orElse(null);
			if (apartmentIndexOld == null) 
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_APARTMENT_INDEX), HttpStatus.NOT_FOUND);
			
			// Get price paking
			PriceParking bicyclePrice =  priceParkingRepository.findFirstByDateLessThanEqualAndTypeVehicelOrderByDateDesc(
																request.getDate(),typeVehicelRepository.findByName(NameTypeVehicel.BICYCLE).orElse(null) )
														     	.orElse(null);
			if (bicyclePrice == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_BICYCEL), HttpStatus.NOT_FOUND);
			
			PriceParking motocyclePrice =  priceParkingRepository.findFirstByDateLessThanEqualAndTypeVehicelOrderByDateDesc(
																  request.getDate(),typeVehicelRepository.findByName(NameTypeVehicel.MOTOCYCLE).orElse(null) )
																 .orElse(null);
			if (motocyclePrice == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_MOTOCYCEL), HttpStatus.NOT_FOUND);
			
			PriceParking carPrice =  priceParkingRepository.findFirstByDateLessThanEqualAndTypeVehicelOrderByDateDesc(
															request.getDate(),typeVehicelRepository.findByName(NameTypeVehicel.CAR).orElse(null) )
														   .orElse(null);
			if (carPrice == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_CAR), HttpStatus.NOT_FOUND);
			
			
			// Get price water
			PriceWater priceWater = priceWaterRepository.findFirstByDateLessThanEqualOrderByDateDesc(request.getDate()).orElse(null);
			if (priceWater == null) 
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_WATER), HttpStatus.NOT_FOUND);
				
			// Get price manage
			PriceManagement managementPrice = priceManagementRepository.findFirstByDateLessThanEqualOrderByDateDesc(request.getDate())
																	   .orElse(null);
			if (managementPrice == null)
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_MANAGEMENT), HttpStatus.NOT_FOUND);
			
			// Get price garbage
			PriceGarbage priceGarbage = priceGarbageRepository.findFirstByDateLessThanEqualOrderByDateDesc(request.getDate())
																		.orElse(null);
			if (priceGarbage == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_PRICE_GARBAGE), HttpStatus.NOT_FOUND);
					
		
			//Get price electricity
			PriceElectricity electricityPrice_1 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																	 request.getDate(),ElectricityLimits.LIMIT_1)
																	 .orElse(null);
			if (electricityPrice_1 == null)
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_1), HttpStatus.NOT_FOUND);
			
			PriceElectricity electricityPrice_2 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 request.getDate(),ElectricityLimits.LIMIT_2)
																			 .orElse(null);
			if (electricityPrice_2 == null)
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_2), HttpStatus.NOT_FOUND);
			
			PriceElectricity electricityPrice_3 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 request.getDate(),ElectricityLimits.LIMIT_3)
																			 .orElse(null);
			if (electricityPrice_3 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_3), HttpStatus.NOT_FOUND);
			
			PriceElectricity electricityPrice_4 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 request.getDate(),ElectricityLimits.LIMIT_4)
																			 .orElse(null);
			if (electricityPrice_4 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_4), HttpStatus.NOT_FOUND);
						
			PriceElectricity electricityPrice_5 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
																			 request.getDate(),ElectricityLimits.LIMIT_5)
																			 .orElse(null);
			if (electricityPrice_5 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_5), HttpStatus.NOT_FOUND);
						
			PriceElectricity electricityPrice_6 = priceElectricityRepository.findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(
					request.getDate(),ElectricityLimits.LIMIT_6)
					 .orElse(null);
			if (electricityPrice_6 == null)
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_ELECTRICITY_LIMIT_6), HttpStatus.NOT_FOUND);
				
			
			ApartmentIndex exApartmentIndex = apartmentIndexRepository.findByMonthInYear(apartmentIndexOld.getApartment().getId(),
					year ,month +1).orElse(null);		

			if (exApartmentIndex !=  null && exApartmentIndex.getId() != id ) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_APARTMENT_INDEX), HttpStatus.CONFLICT);
			}
					
			if (request.getDate().getMonth() == 0) {
				month = 12;
				year = year -1;
			}
			
			// Get old previous
			ApartmentIndex apartmentIndexprevious = apartmentIndexRepository.findByMonthInYear(apartmentIndexOld.getApartment().getId(), year,month).orElse(null);		
			List<ApartmentIndex> apartmentIndexsPrevious  = apartmentIndexRepository.findByDateLessThan(apartmentIndexOld.getApartment().getId(), year,month+1);

		    if (apartmentIndexsPrevious.size() <= 0) {
		    	waterNumber  =  request.getWarterNumber();
			    electricityNumber = request.getElectricityNumber();	 
			}else {

			if (apartmentIndexprevious == null || apartmentIndexprevious.getId() == id ) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_OLD_APARTMENT_INDEX), HttpStatus.NOT_FOUND);
			}
				
			// Check the number is smaller than the old number
			if (  request.getElectricityNumber() < apartmentIndexprevious.getNewElectricityNumber() ) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_LECTRICITY_NUMBER), HttpStatus.CONFLICT);
			}
			if (  request.getWarterNumber()  < apartmentIndexprevious.getNewWaterNumber() ) {
				return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_409_WATER_NUMBER ), HttpStatus.CONFLICT);
			}
			
		 	 waterNumber  =  request.getWarterNumber() -  apartmentIndexprevious.getNewWaterNumber();
		     electricityNumber = request.getElectricityNumber() - apartmentIndexprevious.getNewElectricityNumber();
			}	
			
		     
		     
		     
		     
		     
		     
			
			// Insert into table ApartmentIndex
			ApartmentIndex apartmentIndex = new ApartmentIndex(id, request.getElectricityNumber(), request.getWarterNumber(), request.getBicycleNumber(),
														request.getMotocycleNumber(), request.getCarNumber(), request.getDate(), 
														apartmentIndexOld.getApartment(), request.getEmployee());	
			
			apartmentIndex = apartmentIndexRepository.save(apartmentIndex);
		
						
		   // Insert into table Bills							
			Bill bill = new Bill(apartmentIndex.getId(), electricityNumber, electricityPrice_1.getPrice(), electricityPrice_2.getPrice(), electricityPrice_3.getPrice(),
					electricityPrice_4.getPrice(), electricityPrice_5.getPrice(), electricityPrice_6.getPrice(),0.0, waterNumber, priceWater.getPrice(),
					apartmentIndex.getBicycleNumber(), bicyclePrice.getPrice(), apartmentIndex.getMotocycleNumber(), motocyclePrice.getPrice(),
					apartmentIndex.getCarNumber(), carPrice.getPrice(), 0.0, managementPrice.getPrice(),priceGarbage.getPrice(), 0,
					false, apartmentIndex);
			bill.setElectricityPriceTotal(electricityPriceTotal(bill));
			bill.setParkingPriceTotal(parkingPriceTotal(bill));
			bill.setTotalPrice(calculatorTotal(bill));	
			bill =  billRepository.save(bill);
			
			return ResponseEntity.ok(new ResponseDTO(bill, MessageSuccess.UPDATE_SUCCSESS));
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	 // < --------------------------------- Delete -----------------------------------> 
    public ResponseEntity<ResponseDTO> delete(int id) {
        try {  	
            if (!apartmentIndexRepository.existsById(id))
                return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_APARTMENT_INDEX), HttpStatus.NOT_FOUND);
            
            Bill bill = billRepository.findById(id).orElse(null);
            if (bill != null && bill.getPaid()) 
            	return new ResponseEntity<>(new ResponseDTO(null, MessageError.DELETE_FAIL), HttpStatus.CONFLICT);   

            billRepository.deleteById(id);
            apartmentIndexRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.DELETE_SUCCSESS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	public ResponseEntity<ResponseDTO> payment(int id, boolean paid, int id_nv) {
		try {
		Bill bill = billRepository.findById(id).orElse(null);
		if (bill == null) 		
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_404_BILL), HttpStatus.NOT_FOUND);				
			
		ApartmentIndex apartmentIndex = apartmentIndexRepository.findById(id).orElse(null);
		Employee employee = new Employee();
			
		apartmentIndex.setEmployee(employee);
		employee.setId(id_nv);	
		
		bill.setPaid(paid);
		billRepository.save(bill);	
		apartmentIndexRepository.save(apartmentIndex);
		
			return ResponseEntity.ok(new ResponseDTO(null, MessageSuccess.UPDATE_SUCCSESS));
		} catch (Exception e) {
			
			return new ResponseEntity<>(new ResponseDTO(null, MessageError.ERROR_500), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public ResponseEntity<List<Integer>> findAllMonth(int year) {
		
		try {
			return ResponseEntity.ok(apartmentIndexRepository.findALLMonth(year));
		} catch (Exception e) {
			
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	public ResponseEntity<List<Integer>> findAllYear() {
		
		try {
			return ResponseEntity.ok(apartmentIndexRepository.findALLYear());
		} catch (Exception e) {
			
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	
	
	
	
	public int calculatorTotal(Bill bill) {	
			
	   double total = bill.getElectricityPriceTotal() +  bill.getWaterNumber()*bill.getWaterPrice()+ bill.getParkingPriceTotal() + bill.getManagementPrice() + bill.getGarbagesPrice();

	   return (int) total ;
	}
	
	
    public double electricityPriceTotal(Bill bill) {

		double priceElectricity = ( getInt(bill.getElectricityNumber(), 0, ElectricityLimits.LIMIT_1)*bill.getElectricityPrice1()
				 + getInt(bill.getElectricityNumber(), ElectricityLimits.LIMIT_1, ElectricityLimits.LIMIT_2)*bill.getElectricityPrice2()
				 + getInt(bill.getElectricityNumber(), ElectricityLimits.LIMIT_2, ElectricityLimits.LIMIT_3)*bill.getElectricityPrice3()
				 + getInt(bill.getElectricityNumber(), ElectricityLimits.LIMIT_3, ElectricityLimits.LIMIT_4)*bill.getElectricityPrice4()
				 + getInt(bill.getElectricityNumber(), ElectricityLimits.LIMIT_4, ElectricityLimits.LIMIT_5)*bill.getElectricityPrice5()
				 + getInt(bill.getElectricityNumber(), ElectricityLimits.LIMIT_5, 9999)*bill.getElectricityPrice6())*1.1;
		
		return (int) priceElectricity;
		
	}
	
    public double parkingPriceTotal(Bill bill) {
		double priceParking =   bill.getCarNumber()*bill.getCarPrice() 
				+ bill.getBicycleNumber()*bill.getBicyclePrice() + bill.getMotocycleNumber()*bill.getMotocyclePrice();
		
		return (int) priceParking;
		
	}
    

	public int getInt(int number, int start, int end){
		  if (number <= start) return 0;
		  if (number >= end) return end-start;
		  return number - start;  //number < end
		}
	

	

}
