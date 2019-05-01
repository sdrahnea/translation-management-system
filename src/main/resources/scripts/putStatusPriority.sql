/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  sdrahnea
 * Created: Feb 7, 2017
 */

update status set priority = 1 where name = 'NEW';
update status set priority = 2 where name = 'INFORMED';
update status set priority = 3 where name = 'READY_TO_WORK';
update status set priority = 4 where name = 'ASSIGNED';
update status set priority = 5 where name = 'DELIVERED';
update status set priority = 6 where name = 'INVOICED';
update status set priority = 7 where name = 'CLIENT_PAID';
update status set priority = 8 where name = 'TRANSLATOR_PAID';
update status set priority = 9 where name = 'PAID';
update status set priority = 10 where name = 'ARCHIVED';
update status set priority = 11 where name = 'RESTORED';
update status set priority = 0 where name = 'Select One';