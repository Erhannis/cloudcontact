security/legal concerns
  name, dob, email, phone are PII
  cookies warning
  ugh, ToS
  semi-public aliases?  for pre-match naming.
  resolve blah+X@address.com aliases, against ban avoidance?

misc concerns
  materials
    the more digital, the easier it will be on me
    BUT digital isn't as physically present
  assistants/team?
    bleh, finding people - but it'd help the event go well, I expect
  consider that conventions have bad internet; might NEED app
    "progressive web app", service workers
    text message notifications
  provide users with detailed flowchart, as below?
  maybe have some manga at the Hangout Couch  :P
    should probably limit the number of hangers-out to a reasonable number.  :P

events contain
  sessions contain
    dates

user process
  qr code, link -> schedule?event=blah (-> registration, if not logged in)
    should show "about" instead?  or also, at top?
  register
  user give stats and preferences
    NOTE: consider both what info we NEED, and how to protect it
    should be part of registration?
    after reg, but before scheduling
    phone # for notifications?  optional, but strongly recommended.  ("if miss, penalty")
    "what should be revealed to a match" (email, name, twitter handle, phone number, etc.  default is email?  provide text field)
  user provide proof of event attendance
    (probably don't list events on site, at least not until after they occur, so they can't as easily be searched)
    qr code?  simple passcode?
    take one (qr) code out of stack, one-time use?
    user provide proof of id, for bans and accountability?
      that gets...legally tricky.
  site show timeslots
  user select available ranges
    check boxes out of available timegrid?
  user provide max count
    ("How many sessions can you attend?  sessions are an hour[?] long, containing 6 ten-minute speed dates.")
      Do we have instructions at the beginning?
      Probably better to put it 
    also preferred count?
    more complex criteria?
    min notice time?
  at some point, gen schedule
    day of?
    N hours in advance?
    continuous, but fixed later?
    they probably won't be told their pairs until it's time - just when to show up
      recommend them to check schedule every N-1 hours?
    criteria:
      fairness (tempered by demand)
      max sessions
      no repeats
        ...unless you mark a person as "can repeat"?
  site alerts user of schedule
    text message and email?
    "please show up at room X at time Y; your date tables, in order, are A, B, C, D, E, and F."
      map time to table?  "6:00 - A\n6:10 - B..."
    also, the hangout couch for slack I can't fill, and for stood-ups; allow autonomous pair-off
  user shows up
    scan QR at entry to confirm attendance?
    user texts a code?
    or provide badge/name/email/id?
  (user misses appointment?)
    flag user; two strikes you're out?
      recover after a year or two?  or after three events?
    if can't check id, may be unenforceable anyway
  (user cancels)
    still a problem, but I appreciate their honesty
    maybe...you get one free cancel per event?  then it turns to half-flags?  two-thirds flags?
  big sign says check your schedule
    I may not be allowed to provide a person's schedule (without proof of identity), lest a schedule be stolen
      also that's work
    ...how much help am I willing to provide?
      get team?
  user sits down at table
    date sits down at table
    allowed to flag date AWOL at the 1:00.000 mark?
      stood-up is then directed to the Hangout Couch for sympathy and e.g. manga
      what if a user is absent for 1 date out of 6?  partial flag?
    can flag a user for bad behavior?
      hard to verify.  action at three incidents?
      maybe we can just leave that to the con staff!  yay!
    talk for 9:30.000
    can mark a date a match
      allow post-hoc marking - identification may be hard for them, but that's kinda on them
        should we allow semi-public aliases?  ehhh....hmm.
      if both match, match-info is revealed to the user
      maaaaybe they're allowed to unmatch.  that's not info-theory secure, though.
        I dunno, I don't really wanna support peeking.  be fair.
	the recourse I can probably give them is "delete your account, try again next year".
	  would result in flags.
	maybe support unmatching after a timeout?  like, a week?
	  maybe I'll add that later.
    can take notes?
      eh.  maybe later.  bring paper, you lazies.
      it'd probably be a good addition, but it's not critical.
    buzzer goes off / I yell
    time for next date
  then they're done, and they leave
    and GET MARRIED AND LIVE HAPPILY EVER AFTER WITH THEIR NEW SOULMATE ;P
    

out-of-flow events
  report incidents?  (ugh, then I'd have to DEAL with incidents...)
  