SELECT * 
FROM ScrumMeeting join TeamMember 
on TeamMember.teamID = ScrumMeeting.teamID 
where ScrumMeeting.teamID = 1
and TeamMember.memberID = 1
order by date 
limit 1;