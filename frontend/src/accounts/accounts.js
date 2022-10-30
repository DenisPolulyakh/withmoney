import React from 'react';
import {
    Create,
    CreateButton,
    Datagrid,
    Edit,
    List,
    ReferenceInput, required, SelectInput,
    SimpleForm,
    TextField,
    TextInput,
    TopToolbar
} from 'react-admin';

const AccountActions = () => (
    <TopToolbar>
        <CreateButton/>
    </TopToolbar>
);
export const Accounts = props => (
    <List actions={<AccountActions/>}{...props}>
        <Datagrid rowClick='edit'>
            <TextField source='id'/>
            <TextField source='nameAccount'/>
            <TextField source='currency'/>
            <TextField source='description'/>
        </Datagrid>
    </List>
);

export const AccountEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source='nameAccount'  label='Наименование счёта'/>
            <TextInput source='currency' label='Валюта счёта'/>
            <TextInput source='description' label='Описание' multiline/>
        </SimpleForm>
    </Edit>
);

export const AccountCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source='nameAccount' label='Наименование счёта'/>
            <TextInput source='currency' label='Валюта счёта'/>
            <TextInput source='description' label='Описание' multiline/>
        </SimpleForm>
    </Create>
);

