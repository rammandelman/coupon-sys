		/*
		 * this function switches between the functions displayed to the admin user
		 */
		function switchContent(type){
			const adminFunctions = document.querySelector('#admin-functions');
			const compFunctions = document.querySelector('#comp-functions');
			const custFunctions = document.querySelector('#cust-functions');
			adminFunctions.classList.add('hide');
			compFunctions.classList.add('hide');
			custFunctions.classList.add('hide');
			// admin_CreateComp.classList.add('hide');
			switch(type) {
				case 'admin':
					adminFunctions.classList.remove('hide');
					break;
				case 'comp':
					compFunctions.classList.remove('hide');
					break;
				case 'cust':
					custFunctions.classList.remove('hide');
					break;

				default:
					break;
			}
		}
		function changeFiledOption(type){
			const stringFiled = document.querySelector('#string-filed');
			const couponType = document.querySelector('#coupon-enum-filed');
			
			stringFiled.classList.add('hide');
			couponType.classList.add('hide');
			
			// admin_CreateComp.classList.add('hide');
			if(type == 'type'){
				couponType.classList.remove('hide');
			}else{
				stringFiled.classList.remove('hide');
			}
			
		}
		 /*
		  * this function get a a compnay by ID from DB
		  */
		function getCompany() {
			console.log("this is get comp func in js")
			const id = document.querySelector('#comp-id-input').value;
			const compList = document.querySelector('#comp-list');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/comp/getCompany?id=${id}`,
				function(data) {
				
					while (compList.hasChildNodes()) {
						compList.removeChild(compList.lastChild);
					}
					
					if(typeof data === 'string') {
						alert(data);
					}else{
						
					
					for (const key in data) {
						const value = data[key];
						const listItem = document.createElement('li');
						const itemLabel = document.createElement('b');
						const itemValue = document.createElement('span');
						itemLabel.innerText = `${key}: `;
						itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
						listItem.appendChild(itemLabel);
						listItem.appendChild(itemValue);
						compList.appendChild(listItem);
					}}
				});
		}
		function getCoupon() {
			console.log("this is get coupon func in js")
			const couponID = document.querySelector('#coupon-serach-id-input').value;
			const couponList = document.querySelector('#coupon-search-list');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/coupons/getCoupon?couponID=${couponID}`,
				function(data) {
				console.log(data);
					while (couponList.hasChildNodes()) {
						couponList.removeChild(couponList.lastChild);
					}
					
					if(typeof data === 'string') {
						alert(data);
					}else{
						
					
					for (const key in data) {
						const value = data[key];
						const listItem = document.createElement('li');
						const itemLabel = document.createElement('b');
						const itemValue = document.createElement('span');
						itemLabel.innerText = `${key}: `;
						itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
						listItem.appendChild(itemLabel);
						listItem.appendChild(itemValue);
						couponList.appendChild(listItem);
					}}
				});
		}
		function getCustomer() {
			console.log("this is get cust func in js")
			const custID = document.querySelector('#cust-id-input').value;
			const custList = document.querySelector('#cust-by-id');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/cust/getCustomer?custID=${custID}`,
				function(data) {
					while (custList.hasChildNodes()) {
						custList.removeChild(custList.lastChild);
					}
					if(typeof data === 'string') {
						alert(data);
					}else{
									
					for (const key in data) {
						const value = data[key];
						const listItem = document.createElement('li');
						const itemLabel = document.createElement('b');
						const itemValue = document.createElement('span');
						itemLabel.innerText = `${key}: `;
						itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
						listItem.appendChild(itemLabel);
						listItem.appendChild(itemValue);
						custList.appendChild(listItem);
					}}
				});
		}
		function getAllCompanies() {
			const allCompLists = document.querySelector('#all-comp-lists');
			$.getJSON("http://localhost:8080/finallProjectWeb/rest/comp/getAllCompanies",
				function(dataArr) {
				console.log(dataArr);
					while (allCompLists.hasChildNodes()) {
						allCompLists.removeChild(allCompLists.lastChild);
					}
					if(typeof dataArr === 'string') {
						alert(dataArr);
					}else{
					for(const data of dataArr){
						const compList = document.createElement('ul');
						for (const key in data) {
							const value = data[key];
							const listItem = document.createElement('li');
							const itemLabel = document.createElement('b');
							const itemValue = document.createElement('span');
							itemLabel.innerText = `${key}: `;
							itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
							listItem.appendChild(itemLabel);
							listItem.appendChild(itemValue);
							compList.appendChild(listItem);
						}
						
						allCompLists.appendChild(compList);
					}}
				});
		}
			
		
		function getAllCoupons() {
			const allCouponsList = document.querySelector('#all-coupons-lists');
			$.getJSON("http://localhost:8080/finallProjectWeb/rest/coupons/getAllCoupons",
				function(dataArr) {
					while (allCouponsList.hasChildNodes()) {
						allCouponsList.removeChild(allCouponsList.lastChild);
					}
					if(typeof dataArr === 'string') {
						alert(dataArr);
					}else{
					for(const data of dataArr){
						const couponList = document.createElement('ul');
						for (const key in data) {
							const value = data[key];
							const listItem = document.createElement('li');
							const itemLabel = document.createElement('b');
							const itemValue = document.createElement('span');
							itemLabel.innerText = `${key}: `;
							itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
							listItem.appendChild(itemLabel);
							listItem.appendChild(itemValue);
							couponList.appendChild(listItem);
						}
						
						allCouponsList.appendChild(couponList);
					}}
				});
		}
		function getCouponByType() {
			const type = document.querySelector('#search-CouponType').value;
			const allCouponsByTypeLists = document.querySelector('#all-coupons-by-type-lists');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/coupons/getCouponByType?type=${type}`,
				function(dataArr) {
					while (allCouponsByTypeLists.hasChildNodes()) {
						allCouponsByTypeLists.removeChild(allCouponsByTypeLists.lastChild);
					}
					if(typeof dataArr === 'string') {
						alert(dataArr);
					}else{
					for(const data of dataArr){
						const couponList = document.createElement('ul');
						for (const key in data) {
							const value = data[key];
							const listItem = document.createElement('li');
							const itemLabel = document.createElement('b');
							const itemValue = document.createElement('span');
							itemLabel.innerText = `${key}: `;
							itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
							listItem.appendChild(itemLabel);
							listItem.appendChild(itemValue);
							couponList.appendChild(listItem);
						}
						
						allCouponsByTypeLists.appendChild(couponList);
					}}
				});
		}
		function getPurchasedCouponByType() {
			console.log('this getPurchasedCouponByType from js')
			const custID = document.querySelector('#purchased-coupon-by-type-custID').value;
			const couponType = document.querySelector('#search-purchased-CouponType').value;
			const allCouponsByTypeLists = document.querySelector('#purchased-coupon-by-type-list');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/coupons/getPurchasedCouponByType?
			custID=${custID}&couponType=${couponType}`,
			function(dataArr) {
			while (allCouponsByTypeLists.hasChildNodes()) {
				allCouponsByTypeLists.removeChild(allCouponsByTypeLists.lastChild);
			}
			if(typeof dataArr === 'string') {
				alert(dataArr);
			}else{
			for(const data of dataArr){
				const couponByType = document.createElement('ul');
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					couponByType.appendChild(listItem);
				}
				
				allCouponsByTypeLists.appendChild(couponByType);
			}}
		});
		}
		
		function getPurchasedCouponByPrice() {
			console.log('this is js getPurchasedCouponByPrice ')
			const custID = document.querySelector('#purchased-coupon-by-price-custID').value;
			const PRICE = document.querySelector('#purchased-coupon-by-price-price').value;
			const allCouponsByTypeLists = document.querySelector('#purchased-coupon-by-price-list');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/coupons/getPurchasedCouponByPrice?
			custID=${custID}&PRICE=${PRICE}`,
				function(dataArr) {
					while (allCouponsByTypeLists.hasChildNodes()) {
						allCouponsByTypeLists.removeChild(allCouponsByTypeLists.lastChild);
					}
					console.log(dataArr);
					if(typeof dataArr === 'string') {
						alert(dataArr);
					}else{
					for(const data of dataArr){
						const couponList = document.createElement('ul');
						for (const key in data) {
							const value = data[key];
							const listItem = document.createElement('li');
							const itemLabel = document.createElement('b');
							const itemValue = document.createElement('span');
							itemLabel.innerText = `${key}: `;
							itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
							listItem.appendChild(itemLabel);
							listItem.appendChild(itemValue);
							couponList.appendChild(listItem);
						}
						
						allCouponsByTypeLists.appendChild(couponList);
					}}
				});
		}
		function getAllCustomers() {
			const allCustLists = document.querySelector('#all-cust-lists');
			$.getJSON("http://localhost:8080/finallProjectWeb/rest/cust/getAllCustomers",
				function(dataArr) {
					while (allCustLists.hasChildNodes()) {
						allCustLists.removeChild(allCustLists.lastChild);
					}
					if(typeof dataArr === 'string') {
						alert(dataArr);
					}else{
					for(const data of dataArr){
						const custList = document.createElement('ul');
						for (const key in data) {
							const value = data[key];
							const listItem = document.createElement('li');
							const itemLabel = document.createElement('b');
							const itemValue = document.createElement('span');
							itemLabel.innerText = `${key}: `;
							itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
							listItem.appendChild(itemLabel);
							listItem.appendChild(itemValue);
							custList.appendChild(listItem);
						}
						
						allCustLists.appendChild(custList);
					}}
				});
		}		

		
		function getCoupons() {
			const companieID = document.querySelector('#couponOf-companieID').value;
			const couponsList = document.querySelector('#coupons-listfor-comp');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/comp/getCoupons?companieID=${companieID}`,
				function(dataArr) {
					while (couponsList.hasChildNodes()) {
						couponsList.removeChild(couponsList.lastChild);
					}
					if(typeof dataArr === 'string') {
						alert(dataArr);
						console.log(dataArr);
						console.log('from js exception is thrown')
					}else{
					for(const data of dataArr){
						const coupons = document.createElement('ul');
						for (const key in data) {
							const value = data[key];
							const listItem = document.createElement('li');
							const itemLabel = document.createElement('b');
							const itemValue = document.createElement('span');
							itemLabel.innerText = `${key}: `;
							itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
							listItem.appendChild(itemLabel);
							listItem.appendChild(itemValue);
							coupons.appendChild(listItem);
						}
						
						couponsList.appendChild(coupons);
					}}
				});
		}
		
		function createCompany() {
			console.log('this is js create comp');
			const companieID = document.querySelector('#companieID').value;
			const COMP_NAME = document.querySelector('#COMP_NAME').value;
			const EMAIL = document.querySelector('#EMAIL').value;
			const Password = document.querySelector('#Password').value;			
			const newComplist =  document.querySelector('#newComp-list');	
			console.log(companieID + COMP_NAME+ EMAIL + Password);
			 $.getJSON(`http://localhost:8080/finallProjectWeb/rest/comp/createCompany?companieID=${companieID}&COMP_NAME=${COMP_NAME}&EMAIL=${EMAIL}&Password=${Password}`,				
					function(data) {	
				 		
						while (newComplist.hasChildNodes()) {
							newComplist.removeChild(newComplist.lastChild);
						}						
						if(typeof data === 'string') {
							alert(data);
						} else {
							for (const key in data) {
								const value = data[key];
								const listItem = document.createElement('li');
								const itemLabel = document.createElement('b');
								const itemValue = document.createElement('span');
								itemLabel.innerText = `${key}: `;
								itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
								listItem.appendChild(itemLabel);
								listItem.appendChild(itemValue);
								newComplist.appendChild(listItem);
							}
						}
						
						
					});				
		}	
		
		function removeCompany() {
			console.log('this is js removeCompany')
			const compID = document.querySelector('#companieID-to-remove').value;
			
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/comp/removeCompany?compID=${compID}`,
					function(data){
					alert(data);
		});
			
		}
		function removeCoupon() {
			console.log('this is js removeCoupon')
			const couponID = document.querySelector('#couponID-to-remove').value;
			
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/comp/removeCoupon?couponID=${couponID}`,
					function(data){
				alert(data);
			
			});
			
		}
		function removeCustomer() {
			console.log('this is js removeCustomer')
			const custID = document.querySelector('#custID-to-remove').value;
			
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/cust/removeCustomer?custID=${custID}`,
					function(data){
				alert(data);
			
			}
			);
			
		}
		function createCustomer() {
			console.log('this is js create cust')
			const custID = document.querySelector('#create-cust-id').value;
			const CUST_NAME = document.querySelector('#create-cust-CUST_NAME').value;
			const PASSWORD = document.querySelector('#create-cust-PASSWORD').value;	
			const createdCustomer = document.querySelector('#created-customer');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/cust/createCustomer?custID=${custID}&CUST_NAME=${CUST_NAME}&PASSWORD=${PASSWORD}`,
					function(data) {
				while (createdCustomer.hasChildNodes()) {
					createdCustomer.removeChild(createdCustomer.lastChild);
				}	
				if(typeof data === 'string') {
					alert(data);
				}else{
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					createdCustomer.appendChild(listItem);
				}}
			});
			
		}
		function purchase() {
			console.log("this is js purchase func")
			const couponID = document.querySelector('#couponID-to-purchase').value;
			const custID = document.querySelector('#custID-purchase').value;
			const coupon = document.querySelector('#coupon');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/cust/purchase?couponID=${couponID}&custID=${custID}`,
					

				function(data) {
				
					while (coupon.hasChildNodes()) {
						coupon.removeChild(coupon.lastChild);
					}
					if(typeof data === 'string') {
						alert(data);
					}else{
					for (const key in data) {
						const value = data[key];
						const listItem = document.createElement('li');
						const itemLabel = document.createElement('b');
						const itemValue = document.createElement('span');
						itemLabel.innerText = `${key}: `;
						itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
						listItem.appendChild(itemLabel);
						listItem.appendChild(itemValue);
						coupon.appendChild(listItem);
						
					}}	
				});
		}
		function updateCoupon() {
			console.log('this is js updateCoupon')
			const couponID = document.querySelector('#coupon-id-input-to-update').value;
			const companieID = document.querySelector('#compID-name-update').value;
			
			const TITLE = document.querySelector('#coupon-TITLE-to-update').value;
			const START_DATE= document.querySelector('#coupon-START_DATE-to-update').value;
			const END_DATE = document.querySelector('#coupon-END_DATE-to-update').value;
			const AMOUNT = document.querySelector('#coupon-AMOUNT-to-update').value;
			const MESSAGE = document.querySelector('#coupon-MESSAGE-to-update').value;
			const PRICE = document.querySelector('#coupon-PRICE-to-update').value;
			const IMAGE = document.querySelector('#coupon-IMAGE-to-update').value;
			const type = document.querySelector('#coupon-type-to-update').value;

			const tech =  document.querySelector('#updated-CouponType-tech').value;
			const food =  document.querySelector('#updated-CouponType-food').value;
			const entertainment =  document.querySelector('#updated-CouponType-entertainment').value;
			const shooping =  document.querySelector('#updated-CouponType-shooping').value;
			const kids =  document.querySelector('#updated-CouponType-kids').value;
			const filedToChange = document.querySelector('#couponFiledToChange').value;
			const updateCouponType = document.querySelector('#update-CouponType').value;
			const stringNewValue = document.querySelector('#coupon-filed-newValue').value;
			const updatedCoupon = document.querySelector('#updated-coupon-list');
			const newValue =null;
			console.log(filedToChange);
			if(filedToChange === 'type'){				
			console.log('true');
			console.log(updateCouponType);
			 this.newValue = updateCouponType;
			console.log(this.newValue);
			}else{
				this.newValue = stringNewValue;
			}
			console.log(this.newValue);
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/coupons/updateCoupon?couponID=${couponID}&filedToChange=${filedToChange}&newValue=${this.newValue}`,
					function(data) {
				
				while (updatedCoupon.hasChildNodes()) {
					updatedCoupon.removeChild(updatedCoupon.lastChild);
				}
				if(typeof data === 'string') {
					alert(data);
				}else{		
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					updatedCoupon.appendChild(listItem);
					
				}}
			}
			
			);
			console.log("from js func, the coupon id recived is: " +couponID +' the filedToChange is:  ' + filedToChange+" the new val is: "+this.newValue);
		}
		function updateCompany() {
			console.log('this is js updateCompany')
			const compID = document.querySelector('#comp-id-input-to-update').value;
			const COMP_NAME = document.querySelector('#comp-name-update').value;
			const EMAIL = document.querySelector('#EMAIL-to-update').value;
			const Password = document.querySelector('#password-to-update').value;
			const filedToChange = document.querySelector('#filedToChange').value;
			const newValue = document.querySelector('#comp-filed-newValue').value;
			const updatedCompList = document.querySelector('#updated-comp-list');
			
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/comp/updateCompany?compID=${compID}&filedToChange=${filedToChange}&newValue=${newValue}`,
					function(data){
				while (updatedCompList.hasChildNodes()) {
					updatedCompList.removeChild(updatedCompList.lastChild);
				}
				if(typeof data === 'string') {
					alert(data);
				}else{		
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					updatedCompList.appendChild(listItem);
					
				}
			}
			});
			console.log("from js func, the comp i recived is: " +compID +' the filedToChange is:  ' + filedToChange);
		}
		function updaetCustomer() {
			console.log('this is js updaetCustomer')
			const custID = document.querySelector('#cust-id-input-to-update').value;
			const CUST_NAME = document.querySelector('#cust-name-update').value;
			const Password = document.querySelector('#cust-password-to-update').value;
			const filedToChange = document.querySelector('#cust-filedToChange').value;
			const newValue = document.querySelector('#cust-filed-newValue').value;
			const updatedCust = document.querySelector('#updated-cust');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/cust/updaetCustomer?custID=${custID}&filedToChange=${filedToChange}&newValue=${newValue}`,
					function(data) {
				
				while (updatedCust.hasChildNodes()) {
					updatedCust.removeChild(updatedCust.lastChild);
				}
				if(typeof data === 'string') {
					alert(data);
				}else{	
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					updatedCust.appendChild(listItem);
					
				}}	
			}
					
			);
			//console.log("from js func, the comp i recived is: " +compID +' the filedToChange is:  ' + filedToChange);
		}
		function createCoupon() {
			console.log('this is js createCoupon')
			const couponID =  document.querySelector('#newCoupon-couponID').value;
			const companieID =  document.querySelector('#newCoupon-companieID').value;
			const TITLE =  document.querySelector('#newCoupon-TITLE').value;
			const START_DATE = document.querySelector('#START_DATE').value;
			const END_DATE= document.querySelector('#END_DATE').value;
			const AMOUNT = document.querySelector('#newCoupon-amount').value;
			const MESSAGE = document.querySelector('#newCoupon-MESSAGE').value;
			const PRICE = document.querySelector('#newCoupon-PRICE').value;
			const IMAGE = document.querySelector('#newCoupon-IMAGE').value;
			const type = document.querySelector('#created-CouponType').value;
			const cratedCoupon = document.querySelector('#created-coupon');
			
			const tech =  document.querySelector('#created-CouponType-tech').value;
			const food =  document.querySelector('#created-CouponType-food').value;
			const entertainment =  document.querySelector('#created-CouponType-entertainment').value;
			const shooping =  document.querySelector('#created-CouponType-shooping').value;
			const kids =  document.querySelector('#created-CouponType-kids').value;
			console.log('the coupon id is: ' +couponID +' the type is: ' +type)
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/coupons/createCoupon?couponID=${couponID}&companieID=${companieID}&TITLE=${TITLE}&START_DATE=${START_DATE}&END_DATE=${END_DATE}&AMOUNT=${AMOUNT}&MESSAGE=${MESSAGE}&PRICE=${PRICE}&IMAGE=${IMAGE}&type=${type}`,
					function(data) {
				
				while (cratedCoupon.hasChildNodes()) {
					cratedCoupon.removeChild(cratedCoupon.lastChild);
				}
				if(typeof data === 'string') {
					alert(data);
				}else{	
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					cratedCoupon.appendChild(listItem);
					
				}}	
			}
			
			);
			
		}
		
		 
		function getPurchasedCoupon() {
			console.log("this is get purchaed coupon func in js")
			const PurchasedCoupnByCust = document.querySelector('#purchased-coupon-custID').value;
			const purchasedCouponList = document.querySelector('#purchased-coupon-list');
			$.getJSON(`http://localhost:8080/finallProjectWeb/rest/cust/getCoupons?PurchasedCoupnByCust=${PurchasedCoupnByCust}`,
					function(dataArr) {
				while (purchasedCouponList.hasChildNodes()) {
					purchasedCouponList.removeChild(purchasedCouponList.lastChild);
				}
				if(typeof dataArr === 'string') {
					alert(dataArr);
				}else{
				for(const data of dataArr){
					const coupons = document.createElement('ul');
					for (const key in data) {
						const value = data[key];
						const listItem = document.createElement('li');
						const itemLabel = document.createElement('b');
						const itemValue = document.createElement('span');
						itemLabel.innerText = `${key}: `;
						itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
						listItem.appendChild(itemLabel);
						listItem.appendChild(itemValue);
						coupons.appendChild(listItem);
					}
					
					purchasedCouponList.appendChild(coupons);
				}}
			});
		}
		
		function shutDown() {
			console.log("this is shutDown func in js")
			
			const opnecConnections = document.querySelector('#number-of-open-connections-afterShutDown');
			$.getJSON("http://localhost:8080/finallProjectWeb/rest/admin/shutDown",
				function(data) {
				console.log('form js after function calelled the data is: '+data)
					while (opnecConnections.hasChildNodes()) {
						opnecConnections.removeChild(opnecConnections.lastChild);
					}
									
				document.getElementById("number-of-open-connections-afterShutDown").innerHTML = data;
				});
		}
		
		function checkConnections() {
			console.log('this is js system checkConnections')
			const numberOfOpenConnections = document.querySelector('#number-of-open-connections');
			$.getJSON("http://localhost:8080/finallProjectWeb/rest/admin/checkConnections",
					function(data) {
				console.log('form js after function calelled the data is: '+data)
					while (numberOfOpenConnections.hasChildNodes()) {
						numberOfOpenConnections.removeChild(numberOfOpenConnections.lastChild);
					}
									
				document.getElementById("number-of-open-connections").innerHTML = data;
				});
		}
		