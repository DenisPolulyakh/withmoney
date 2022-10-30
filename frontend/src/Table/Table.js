import React from 'react';

export default props => (
    <table className="table table-striped">
        <thead>
        <tr>
            <th>#</th>
            <th>Наименование категории</th>
            <th>Сумма</th>
            <th>Состояние категории</th>
        </tr>
        </thead>
        <tbody>
        {   props.data.body.map( item =>(
            <tr key={item.id}>
                <td>{item.id}</td>
                <td>{item.nameCategory}</td>
                <td>{item.totalAmount}</td>
                <td>{item.state}</td>
            </tr>
        ))}
        </tbody>
    </table>
)
