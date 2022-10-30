import React from 'react';

import {
    Create,
    Datagrid,
    Edit,
    List,
    ReferenceField,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput,
    TopToolbar,
    CreateButton,
    Pagination,
    useQuery
} from 'react-admin';




const ListActions = () => (
    <TopToolbar>
        <CreateButton/>
    </TopToolbar>
);

export const Categories = props => (
    <List actions={<ListActions/>} {...props}>
        <Datagrid rowClick='edit'>
            <TextField source='nameCategory'/>
            <TextField source='totalAmount'/>
            <TextField source='description'/>
            <ReferenceField source="accountId" link={false} reference="accounts">
                <TextField source="nameAccount"/>
            </ReferenceField>
        </Datagrid>
    </List>
);

export const CategoryEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source='nameCategory'/>
            <TextInput source='totalAmount'/>
            <TextInput source='description' fullWidth multiline />
            <ReferenceInput label="Счёт" source="accountId" reference="accounts" validate={[required()]}>
                <SelectInput optionText="nameAccount"/>
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export const CategoryCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source='nameAccount' label='Наименование счёта'/>
            <TextInput source='nameCategory' label='Нименование категории'/>
            <TextInput source='totalAmount' label='Сумма'/>
            <TextInput source='description' label='Описание'/>
        </SimpleForm>
    </Create>
);

