let format2 = (n) => {
    return n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

let getLimit = (number, start, end) => {
    if (number <= start) return 0;
    if (number >= end) return end - start;
    return number - start;  //number < end
}


var viewDetail = (data) => {
    let limit1 = getLimit(data.electricityNumber, 0, 50);
    let limit2 = getLimit(data.electricityNumber, 50, 100);
    let limit3 = getLimit(data.electricityNumber, 100, 200);
    let limit4 = getLimit(data.electricityNumber, 200, 300);
    let limit5 = getLimit(data.electricityNumber, 300, 400);
    let limit6 = getLimit(data.electricityNumber, 400, 9999);

    return `
	<table class=" table table-striped table-hover" >
						  <thead>
						    <tr>
						     <th>Loại</th>
                       		 <th>Chỉ số cũ</th>
                         	 <th>Chỉ số mới</th>
                             <th>Số lượng</th>
                             <th>Đơn giá  <i>(vnd)</i></th>
                             <th>THUẾ <i>(10%)</i></th>
                             <th>THÀNH TIỀN <i>(vnd)</i></th>
						
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <th scope="row">Quản lý</th>
						      <td></td>
						      <td></td>		
						      <td></td>
						      <td>${format2(data.managementPrice)}</td>
						      <td></td>
						      <td>${format2(data.managementPrice)}</td>
						    </tr>
						    <tr>
						      <th scope="row">Rác</th>
						      <td></td>
						      <td></td>					  
						      <td></td>
						      <td>${format2(data.garbagesPrice)}</td>
						      <td></td>
						      <td>${format2(data.garbagesPrice)}</td>
						    </tr>
						    <tr>
						      <th scope="row">Xe đạp</th>
						      <td ></td>						    
						      <td></td>
						      <td>${data.bicycleNumber}<i> chiếc</i></td>
						      <td>${format2(data.bicyclePrice)}</td>
						      <td></td>
						      <td>${format2(data.bicyclePrice * data.bicycleNumber)}</td>	      	
						    </tr>
						     <tr>
						      <th scope="row">Xe máy</th>
						      <td ></td>
						      <td></td>						    
						      <td>${data.motocycleNumber}<i> chiếc</i></td>
						      <td>${format2(data.motocyclePrice)}</td>
						      <td></td>
						      <td>${format2(data.motocyclePrice * data.motocycleNumber)}</td>	      
						    </tr>
						     <tr>
						      <th scope="row">Xe ô tô</th>
						      <td ></td>
						      <td></td>					   
						      <td>${data.carNumber}<i> chiếc</i></td>
						      <td>${format2(data.carPrice)}</td>
						      <td></td>
						      <td>${format2(data.carPrice * data.carNumber)}</td>	      
						    </tr>
						     <tr>
						      <th scope="row">Nước</th>
						      <td>${data.apartmentIndex.newWaterNumber - data.waterNumber}<i> m3</i></td>
						      <td>${data.apartmentIndex.newWaterNumber}<i> m3</i></td>						     
						      <td>${data.waterNumber}<i> m3</i></td>
						      <td>${format2(data.waterPrice)}</td>
						      <td></td>
						      <td>${format2(data.waterPrice * data.waterNumber)}</td>	      
						    </tr>
						    <tr>
						      <th scope="row">Điện</th>
						      <td>${data.apartmentIndex.newElectricityNumber - data.electricityNumber}<i> kWh</i></td>
						      <td>${data.apartmentIndex.newElectricityNumber}<i> kWh</i></td>						     
						      <td>${data.electricityNumber}<i> kWh</i></td>
						      <td></td>
						      <td></td>
						      <td></td>	      
						    </tr>
						    
						     <!-------------------------- Định mức 1 --------------------------- -->
						    <tr>
						      <td rowspan="6" style="vertical-align : middle;text-align:center;">
						      Đơn giá điện bậc thang <br/>
						      <i>(Đã bao gồm 10% thuế GTGT)</i>
						       </td> 
						      <td></td>
						      <td></td>  
						      <td>${limit1}<i> kWh</i></td>
						      <td>${format2(data.electricityPrice1)}</td>						     
						      <td>${format2(Math.round(data.electricityPrice1 * limit1 * 0.1))}</td>
						      <td>${format2(Math.round(data.electricityPrice1 * limit1 * 1.1))}</td>   
						    </tr>
						    <!-------------------------- Định mức 2 --------------------------- -->
						     <tr>
						      
						      <td></td>
						      <td></td>  
						      <td>${limit2}<i> kWh</i></td>
						      <td>${format2(data.electricityPrice2)}</td>						     
						      <td>${format2(Math.round(data.electricityPrice2 * limit2 * 0.1))}</td>
						      <td>${format2(Math.round(data.electricityPrice2 * limit2 * 1.1))}</td>  
						    </tr>
						    <!-------------------------- Định mức 3 --------------------------- -->
						     <tr>
						     
						      <td></td>
						      <td></td>  
						      <td>${limit3}<i> kWh</i></td>
						      <td>${format2(data.electricityPrice3)}</td>						     
						      <td>${format2(Math.round(data.electricityPrice3 * limit3 * 0.1))}</td>
						      <td>${format2(Math.round(data.electricityPrice3 * limit3 * 1.1))}</td>    
						    </tr>
						    <!-------------------------- Định mức 4 --------------------------- -->
						     <tr>
						     
						      <td></td>
						      <td></td>  
						      <td>${limit4}<i> kWh</i></td>
						      <td>${format2(data.electricityPrice4)}</td>						     
						      <td>${format2(Math.round(data.electricityPrice4 * limit4 * 0.1))}</td>
						      <td>${format2(Math.round(data.electricityPrice4 * limit4 * 1.1))}</td>   
						    </tr>
						    <!-------------------------- Định mức 5 --------------------------- -->
						     <tr>
						      
						      <td></td>
						      <td></td>  
						     <td>${limit5}<i> kWh</i></td>
						      <td>${format2(data.electricityPrice5)}</td>						     
						      <td>${format2(Math.round(data.electricityPrice5 * limit5 * 0.1))}</td>
						      <td>${format2(Math.round(data.electricityPrice5 * limit5 * 1.1))}</td>  
						    </tr>
						    <!-------------------------- Định mức 6 --------------------------- -->
						     <tr>		
						     	      
						      <td></td>
						      <td></td>  
						      <td>${limit6}<i> kWh</i></td>
						      <td>${format2(data.electricityPrice6)}</td>						     
						      <td>${format2(Math.round(data.electricityPrice6 * limit6 * 0.1))}</td>
						      <td>${format2(Math.round(data.electricityPrice6 * limit6 * 1.1))}</td>    
						    </tr>
						    <tr>
						      <th scope="row"><h6><strong>Tổng cộng</strong></h6></th>
						      <td colspan= "5" style="vertical-align : middle;text-align:center;">
						      Lấy tròn  </td>				   
						      <td><h6><strong>${format2(data.totalPrice)}</strong></h6></td>   
						    </tr>
						  </tbody>
					</table>
	`
}