function getAllIncomes() {
			console.log('this is js allIncomes')
			const allIncomesList = document.querySelector('#all-incomes');
			
			$.getJSON("http://localhost:8080/finallProjectWeb/rest/admin/getAllIncomes",
					function(dataArr) {
				console.log('this is js after func called');
					while (allIncomesList.hasChildNodes()) {
						allIncomesList.removeChild(allIncomesList.lastChild);
					}
					if(typeof dataArr === 'string') {
						alert(dataArr);
					}else{
					for(const data of dataArr){
						const incomeList = document.createElement('ul');
						for (const key in data) {
							const value = data[key];
							const listItem = document.createElement('li');
							const itemLabel = document.createElement('b');
							const itemValue = document.createElement('span');
							itemLabel.innerText = `${key}: `;
							itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
							listItem.appendChild(itemLabel);
							listItem.appendChild(itemValue);
							incomeList.appendChild(listItem);
						}
						
						allIncomesList.appendChild(incomeList);
					}}
				});
			
		}
function getAllIncomesByCustName() {
	console.log('this is js getAllIncomesByCustName')
	const allIncomesByCust = document.querySelector('#all-incomes-by-cust-name');
	const custName = document.querySelector('#cust-reports-by-name').value;
	$.getJSON(`http://localhost:8080/finallProjectWeb/rest/admin/getAllIncomesByCustName?custName=${custName}`,
			function(dataArr) {
		console.log('this is js after func called');
		console.log(dataArr);
			while (allIncomesByCust.hasChildNodes()) {
				allIncomesByCust.removeChild(allIncomesByCust.lastChild);
			}
			if(typeof dataArr === null) {
				alert('no incomes found for that name.');
			}else{
			for(const data of dataArr){
				const incomeList = document.createElement('ul');
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					incomeList.appendChild(listItem);
				}
				
				allIncomesByCust.appendChild(incomeList);
			}}
		});
	
}
function getAllIncomesByCompName() {
	console.log('this is js getAllIncomesByCompName')
	const allIncomesByComp = document.querySelector('#all-incomes-by-comp-name');
	const compName = document.querySelector('#comp-reports-by-name').value;
	$.getJSON(`http://localhost:8080/finallProjectWeb/rest/admin/getAllIncomesByCompName?compName=${compName}`,
			function(dataArr) {
		console.log('this is js after func called');
		console.log(dataArr);
			while (allIncomesByComp.hasChildNodes()) {
				allIncomesByComp.removeChild(allIncomesByComp.lastChild);
			}
			if(typeof dataArr === 'string') {
				alert(dataArr);
			}else{
			for(const data of dataArr){
				const incomeList = document.createElement('ul');
				for (const key in data) {
					const value = data[key];
					const listItem = document.createElement('li');
					const itemLabel = document.createElement('b');
					const itemValue = document.createElement('span');
					itemLabel.innerText = `${key}: `;
					itemValue.innerText = Array.isArray(value) ? JSON.stringify(value) : value;
					listItem.appendChild(itemLabel);
					listItem.appendChild(itemValue);
					incomeList.appendChild(listItem);
				}
				
				allIncomesByComp.appendChild(incomeList);
			}}
		});
	
}